var socket = null;
var stompClient = null;

var username = null;
var deleted = false;

var possibleMoves = null;
var gameEnded = false;

var pawnMoveData = "";
const transformBoard = document.getElementById("PawnTransform");

var isWhite = null;
function connect() {
    username = document.querySelector('#name').value.trim();

    if(username) {
        var socket = new SockJS('/websocket');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    document.getElementById("Login").hidden = true;
}

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

function deleteNotations(){
    const circle = document.querySelectorAll('.NotationUl ');
    circle.forEach(circle => {
        circle.remove();
    });
}
deleteNotations();
function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/public', onMessageReceived);
    stompClient.subscribe('/user/topic/private', onMessageReceived);
    stompClient.send("/app/game.start",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )
}


function onError(error) {
    console.log("Error");
}


function Move(event) {
    deleteCircles();

    if(document.getElementById(lastPressedFigureId).getElementsByTagName("img")[0].src.includes("Pawn") && (event.replaceAll('c', '')[0] === "0" || event.replaceAll('c', '')[0] === "7") && event.length<=4){
        pawnMoveData = lastPressedFigureId +"_"+ event.replaceAll('c', '');
        for(const name of ['Queen','Rook','Bishop','Knight']) {
            let button = document.createElement('button');
            button.classList.add("Figure");
            button.onclick = function () {
                Move(event.replaceAll('c', '')+"_"+name);
            };
            let img = document.createElement('img');
            img.src = '/img/' + name + '_'+isWhite +'.png';
            button.appendChild(img);
            transformBoard.appendChild(button)
        }

    }else if(stompClient) {
        var gameMessage = {
            sender: username,
            content: lastPressedFigureId +"_"+ event.replaceAll('c', ''),
        };

        stompClient.send("/app/game.Move", {}, JSON.stringify(gameMessage));
    }
}

function GetMoves(event) {
    if(!gameEnded && document.getElementById(event).getElementsByTagName("img")[0].src.includes(isWhite)) {
        if (lastPressedFigureId !== event) {
            if (stompClient) {
                var gameMessage = {
                    sender: username,
                    content: event,
                };

                stompClient.send("/app/game.getMoves", {}, JSON.stringify(gameMessage));
            }
            lastPressedFigureId = event;
        } else {
            if (document.querySelectorAll('.MoveCircle').length > 0) {
                deleteCircles();
            } else {
                displayPossibleMoves()
            }
        }
    }
}

function displayPossibleMoves(){
    for (const pos of possibleMoves) {
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

function boardDraw(array){
    deleteFigures();
    deleteNotations();
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

    for (let i = 0; i < array.notation.length; i++){
        let ul = document.createElement('ul');
        ul.innerHTML = array.notation[i];
        ul.classList.add("NotationUl");
        if(i%2 === 0) {
            document.getElementById("NotationLeft").appendChild(ul);
        }else {
            document.getElementById("NotationRight").appendChild(ul);
        }
    }
    if(!array.type.includes("Winner")) {
        document.getElementById("PlayerList").innerHTML = "White: " + array.players[0] + "\nBlack: " + array.players[1];
    }else {
        gameEnded = true;
        if(array.type.includes("true")) {
            document.getElementById("PlayerList").innerHTML = "Winner: " + array.players[0]+ "   Player "+ array.players[1]+"was" +array.type.split(" ")[2];
        }else {
            document.getElementById("PlayerList").innerHTML = "Winner: " + array.players[1]+ "   Player "+ array.players[0]+"was "+array.type.split(" ")[2];
        }
    }
}
function onMessageReceived(data) {
    let array = JSON.parse(data.body);
    console.log(array)
    if(!deleted) {
        if (array.players[1] === username) {
            const board = document.querySelector('#WhiteBoard');
            board.remove();
            deleted = true;
        } else {
            const board = document.querySelector('#BlackBoard');
            board.remove();
            deleted = true;
        }
    }

    if(array.type === "BoardLoad" || array.type.includes("Winner")){
        isWhite = username === array.players[0];
        boardDraw(array);
    }
    if(array.type === "PosibleMoves") {
        deleteCircles();
        possibleMoves = array.posibleMoves;
        displayPossibleMoves();
    }
}
