# FEATURE - MAX CHARGE
Players must now have maximum Charge 10.0, from which each turn's `c` is deduced. After both players C reach 0.0, the one who has most points wins
- current player's charge should be always displayed

# BIG FEATURE - WEBSOCKET GAME SERVER
Do decompose this task!
We need a websocket game server for multiplayer matching
- It must be server communicating clients via websocket
- make a `io.github.rb2o2.esg.server` package and place server code there
- use undertow websocket lib
- place server IP to connect to into config file in project root folder named `config.properties`
- matching:
    - a player starts client, it asks whether to connect to a lobby via 4-digit code, or open a lobby,  which should connect on start to server via websocket and recieve a 4-digit random number associated with specific player's client. player tells another player the number, then after second player inputs number code on connection of his client, game starts.
    - each pair of players having unique 4-digit code each whould be tracked separately on server in some sort of state - consider best practices for simultaneous state management
    - on connection error client should return to lobby creation/selection
    - server should send the other player's moves to respective client
    - move button should be disabled after player's move on other player's move, and reenabled after server sends his move
    - after winner is victorious client should display message 'YOU WIN' or 'YOU LOSE' and close websocket connection and remove lobby code and match state
- so after all, client-server messages should be one of:
    - 'CONNECT TO LOBBY'
        - 'CODE'
    - 'NEW LOBBY'
    - 'MOVE'
        - '(0.5,0.5,1.0)' //move details
    - 'WINNER' 


# FEATURE - display both players' charge
- Charge shoud be displayed for both players, each in corresponding color