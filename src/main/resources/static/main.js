var socket = new SockJS('/websocket');
var stompClient = Stomp.over(socket);
var moveCircle = document.querySelector(".MoveCircle");

stompClient.connect({}, onConnected, onError);

var lastPressedFigureId = null;

function deleteCircles(){
    const circle = document.querySelectorAll('.MoveCircle');
    circle.forEach(circle => {
        circle.remove();
    });
}
deleteCircles();

function deleteFigures(){
    const circle = document.querySelectorAll('.Figure ');
    circle.forEach(circle => {
        circle.remove();
    });
}
deleteFigures();
function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);

    stompClient.send("/app/game.start",
        {},
        JSON.stringify({sender: 'user', type: 'JOIN'})
    )
}


function onError(error) {
    console.log("Error");
}


function Move(event) {
    if(lastPressedFigureId !== event){
        if(stompClient) {
            var chatMessage = {
                sender: 'Player1',
                content: lastPressedFigureId +"_"+ event.replaceAll('c', ''),
            };

            stompClient.send("/app/game.Move", {}, JSON.stringify(chatMessage));
        }
        lastPressedFigureId = event;
    }else {
        deleteCircles();
    }
}

function GetMoves(event) {
    if(lastPressedFigureId !== event){
        if(stompClient) {
            var chatMessage = {
                sender: 'Player1',
                content: event,
            };

            stompClient.send("/app/game.getMoves", {}, JSON.stringify(chatMessage));
        }
        lastPressedFigureId = event;
    }else {
        deleteCircles();
    }
}


function onMessageReceived(data) {
    let array = JSON.parse(data.body)
    deleteCircles();
    console.log(array)

    if(array.type === "BoardLoad"){
        deleteFigures();
        for(let i = 0; i < array.board.length; i++){
            for(let j = 0; j < array.board.length; j++){
                if(array.board[i][j] != null) {
                    let button = document.createElement('button');

                    button.classList.add("Figure");
                    button.id = i + "_" + j;
                    button.onclick = function() { GetMoves(this.id); };
                    let img = document.createElement('img');
                    img.src = '/img/' + array.board[i][j]._name +'_'+ array.board[i][j]._white + '.png';
                    button.appendChild(img);
                    document.getElementById("b" + i + "_" + j).appendChild(button);
                }
            }
        }
    }
    if(array.type === "PosibleMoves") {
        for (const pos of array.posibleMoves) {
            let button = document.createElement('button');
            button.classList.add("MoveCircle");
            button.id = "c" + pos[0] + "_" + pos[1];
            button.onclick = function() { Move(this.id); };
            let img = document.createElement('img');
            img.src = '/img/circle.png';
            button.appendChild(img);
            document.getElementById("b" + pos[0] + "_" + pos[1]).appendChild(button);

        }
    }
}
