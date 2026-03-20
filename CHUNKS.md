# FEATURE - MAX CHARGE
- [x] add charge state: MAX_CHARGE 10.0, chargeP1 and chargeP2; deduct c from current player on move
- [x] validate move: reject if c > remaining charge for current player
- [x] display current player's charge in UI (always visible)
- [x] when both charges reach 0.0: determine winner by points, show game over and winner; stop accepting moves

# BIG FEATURE - WEBSOCKET GAME SERVER
- [x] create io.github.rb2o2.esg.server package; add Undertow WebSocket dependency to pom.xml
- [x] add config.properties in project root with server connection IP/key
- [x] implement basic Undertow WebSocket server (start/stop, single endpoint)
- [x] define and parse message types: CONNECT TO LOBBY, CODE, NEW LOBBY, MOVE, WINNER
- [x] server: lobby state (4-digit code per lobby, track pair of clients per code)
- [x] client: on start ask connect-via-code or open lobby; connect to server via WebSocket
- [x] client: open lobby flow -> send NEW LOBBY, receive CODE; show code to user
- [x] client: connect to lobby flow -> send CONNECT TO LOBBY + CODE; server matches and starts game
- [x] server: relay other player's MOVE to respective client
- [x] client: disable move button after own move; reenable when server sends opponent MOVE
- [x] client: on connection error return to lobby creation/selection
- [x] server+client: on winner send WINNER; client shows YOU WIN / YOU LOSE, close connection, clear lobby state

# FEATURE - display both players' charge
- [x] display chargeP1 and chargeP2 simultaneously in corresponding colors (highlighting current player)
