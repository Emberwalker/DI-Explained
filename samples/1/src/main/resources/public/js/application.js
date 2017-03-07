/**
 * Clicker application code.
 *
 * Danger: Here be dragons (Javascript is awful)
 */

var loadingElem = $( "div.loading" );
var mainElem = $( "div.loaded" );
var btn = $( "button.clickme" )[0];
var scoreElem = $( "b.score" )[0];

// Based on https://developer.mozilla.org/en-US/docs/Web/API/Document/cookie
var uuid = document.cookie.replace(/(?:(?:^|.*;\s*)uuid\s*\=\s*([^;]*).*$)|^.*$/, "$1");

var ws = new WebSocket(document.origin.replace("http", "ws") + "/ws");

function sendMsg(msg) {
    console.debug("readyState: ", ws.readyState);
    if (ws.readyState > 1) {
        ws = new WebSocket(document.origin.replace("http", "ws") + "/ws");
        attachWSHandlers();
        ws.onopen = function (event) {
            sendMsg(msg)
        }
    } else {
        ws.send(JSON.stringify(msg));
    }
}

function announceToServer() {
    sendMsg({
        type: 'announce',
        uuid: uuid
    });
}

function attachWSHandlers() {
    ws.onmessage = function (event) {
        var msg = JSON.parse(event.data);
        console.debug(msg);
        switch(msg.type) {
            case 'error':
                switch(msg.reason) {
                    case 'unannounced':
                        announceToServer();
                        break;
                    default:
                        console.error(msg);
                        break;
                }
                break;
            case 'announce_ack':
                console.log("Announcement acknowledged.");
                break;
            case 'score':
                scoreElem.textContent = msg.score;
                break;
            default:
                console.error("Unknown message type: ", msg.type);
                break;
        }
    };
}

btn.onclick = function (event) {
    console.log("Click: ", event);
    sendMsg({
        type: 'click'
    });
};

attachWSHandlers();

loadingElem.hide();
mainElem.removeAttr('style');