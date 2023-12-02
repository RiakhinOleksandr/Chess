var socket = null;
var stompClient = null;

var username = null;
var deleted = false;

var possibleMoves = null;
var gameEnded = false;

var lastPressedFigureId = null;
const transformBoard = document.getElementById("PawnTransform");

var isWhite = null;

function connect() {
    username = document.querySelector('#name').value.trim();

    if(username) {
        socket = new SockJS('/websocket');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
}


function deleteCircles(){
    const circle = document.querySelectorAll('.MoveCircle');
    circle.forEach(circle => {
        circle.remove();
    });
}
deleteCircles();

function deleteFigures(){
    const figures = document.querySelectorAll('.Figure ');
    figures.forEach(figure => {
        figure.remove();
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
    if(lastPressedFigureId) {
        document.getElementById(lastPressedFigureId).style.background = "none";
    }
    if(document.getElementById(lastPressedFigureId).getElementsByTagName("img")[0].src.includes("Pawn") && (event.replaceAll('c', '')[0] === "0" || event.replaceAll('c', '')[0] === "7") && event.length<=4){
        const figures = transformBoard.querySelectorAll('.Figure ');
        figures.forEach(figure => {
            figure.remove();
        });
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
        lastPressedFigureId = null;
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
            if(lastPressedFigureId) {
                document.getElementById(lastPressedFigureId).style.background = "none";
            }
            lastPressedFigureId = event;
            document.getElementById(event).style.background = "rgba(93, 174, 255, 0.45)";
        } else {
            if (document.querySelectorAll('.MoveCircle').length > 0) {
                deleteCircles();
                if(lastPressedFigureId) {
                    document.getElementById(lastPressedFigureId).style.background = "none";
                }
            } else {
                displayPossibleMoves();
                document.getElementById(event).style.background = "rgba(93, 174, 255, 0.45)";
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
        let div = document.createElement('div');
        button.appendChild(div);
        document.getElementById("b" + pos[0] + "_" + pos[1]).appendChild(button);
    }
}

function boardDraw(array){
    deleteFigures();
    deleteNotations();
    document.getElementById("WinInfo").innerHTML = "";
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
            let numUl = document.createElement('ul');
            numUl.innerHTML = "" + (i/2+1);
            numUl.classList.add("NotationUl");
            document.getElementById("NotationNum").appendChild(numUl);
            document.getElementById("NotationLeft").appendChild(ul);
        }else {
            document.getElementById("NotationRight").appendChild(ul);
        }
    }

    if(!array.type.includes("Winner") && !array.type.toLowerCase().includes("draw")) {
        document.getElementById("WhiteTimer").style.opacity = "1";
        document.getElementById("BlackTimer").style.opacity = "1";
        if(array.notation.length%2 === 0){
            document.getElementById("BlackTimer").style.opacity = "0.4";
        }else {
            document.getElementById("WhiteTimer").style.opacity = "0.4";
        }


        document.getElementById("WhiteName").innerHTML =  array.players[0];
        document.getElementById("BlackName").innerHTML =  array.players[1];
    }else {
        if(!array.type.includes("offers a draw")) {
            document.getElementById("WhiteTimer").style.opacity = "1";
            document.getElementById("BlackTimer").style.opacity = "1";
            gameEnded = true;
            document.getElementById("FindNewGame").style.display = "block";
            socket.close();
        }
        if(!array.type.includes(username)) {
            document.getElementById("WinInfo").innerHTML = array.type;
        }
    }
    if(lastPressedFigureId) {
        let temp = lastPressedFigureId;
        lastPressedFigureId = null;
        GetMoves(temp);
    }
}

function secondsToTimerFormat(seconds) {
    var minutes = Math.floor(seconds / 60);
    var remainingSeconds = seconds % 60;

    if (remainingSeconds < 10) {
        remainingSeconds = "0" + remainingSeconds;
    }
    if (minutes < 10) {
        minutes = "0" + minutes;
    }

    return minutes + ":" + remainingSeconds;
}

function onMessageReceived(data) {
    let array = JSON.parse(data.body);
    console.log(array)
    if(!deleted) {
        if (array.players[1] === username) {
            const board = document.querySelector('#WhiteBoard');
            document.querySelector('#Info').style.flexDirection = "column-reverse"
            const playerInfo = document.querySelectorAll('.PlayerInfo');
            playerInfo.forEach(player => {
                player.style.flexDirection = "column-reverse";
            });
            board.remove();
            deleted = true;
        } else {
            const board = document.querySelector('#BlackBoard');
            board.remove();
            deleted = true;
        }
        document.getElementById("Login").style.display = "none";
        document.getElementById("MainPage").style.display = "flex";
    }

    if(array.type === "BoardLoad" || array.type.includes("Winner") || array.type.toLowerCase().includes("draw")){
        isWhite = username === array.players[0];
        boardDraw(array);
    }
    if(array.type === "PosibleMoves") {
        deleteCircles();
        possibleMoves = array.posibleMoves;
        displayPossibleMoves();
    }
    if(array.type === "Timer") {
        const times = array.content.split(' ');
        document.getElementById("WhiteTimer").innerHTML = secondsToTimerFormat(parseInt(times[0]));
        document.getElementById("BlackTimer").innerHTML =  secondsToTimerFormat(parseInt(times[1]));
    }
}

function Resign(text) {
    if(stompClient) {
        var gameMessage = {
            sender: username,
            content: text,
        };
        lastPressedFigureId = null;
        stompClient.send("/app/game.EndGame", {}, JSON.stringify(gameMessage));
    }
}
function ReloadPage(){
    location.reload();
}