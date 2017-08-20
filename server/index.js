var WebSocketServer = require('ws').Server
  , http = require('http')
  , express = require('express')
  , app = express()
  , port = process.env.PORT || 5000;

app.use(express.static(__dirname + '/'));

var server = http.createServer(app);
server.listen(port);

var wss = new WebSocketServer({server: server});
wss.on('connection', function(ws) {
    ws.on('message', function(message) {
        ws.send("Server received: " + message, function() {  });
    });

    ws.on('close', function() {
        console.log('websocket connection close');
    });
});