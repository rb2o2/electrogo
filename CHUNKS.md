# FEATURE - MAX CHARGE
- [x] add charge state: MAX_CHARGE 10.0, chargeP1 and chargeP2; deduct c from current player on move
- [x] validate move: reject if c > remaining charge for current player
- [ ] display current player's charge in UI (always visible)
- [ ] when both charges reach 0.0: determine winner by points, show game over and winner; stop accepting moves

# BIG FEATURE - WEBSOCKET GAME SERVER
- [ ] create io.github.rb2o2.esg.server package; add Undertow WebSocket dependency to pom.xml
- [ ] add config.properties in project root with server connection IP/key
- [ ] implement basic Undertow WebSocket server (start/stop, single endpoint)
- [ ] define and parse message types: CONNECT TO LOBBY, CODE, NEW LOBBY, MOVE, WINNER
- [ ] server: lobby state (4-digit code per lobby, track pair of clients per code)
- [ ] client: on start ask connect-via-code or open lobby; connect to server via WebSocket
- [ ] client: open lobby flow -> send NEW LOBBY, receive CODE; show code to user
- [ ] client: connect to lobby flow -> send CONNECT TO LOBBY + CODE; server matches and starts game
- [ ] server: relay other player's MOVE to respective client
- [ ] client: disable move button after own move; reenable when server sends opponent MOVE
- [ ] client: on connection error return to lobby creation/selection
- [ ] server+client: on winner send WINNER; client shows YOU WIN / YOU LOSE, close connection, clear lobby state
