import React, { useState, useEffect } from 'react';
import { initializeApp } from 'firebase/app';
import { 
  getDatabase, 
  ref, 
  set, 
  onValue, 
  push, 
  update,
  remove,
  off 
} from 'firebase/database';
import './App.css';

// Firebase configuration - REPLACE WITH YOUR OWN CONFIG
const firebaseConfig = {
  apiKey: "AIzaSyDV2eiRcQFnyJ2mWoV5zKz41U3jc9fyPao",
  authDomain: "uno-flip-game.firebaseapp.com",
  databaseURL: "https://uno-flip-game-default-rtdb.firebaseio.com",
  projectId: "uno-flip-game",
  storageBucket: "uno-flip-game.firebasestorage.app",
  messagingSenderId: "166208053986",
  appId: "1:166208053986:web:1fd106c13285e08474f4bb",
  measurementId: "G-DYWZYXJK6V"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const database = getDatabase(app);

// Game Constants
const LIGHT_COLORS = ['Red', 'Green', 'Blue', 'Yellow', 'Pink'];
const DARK_COLORS = ['Teal', 'Purple', 'Orange', 'Pink', 'Cyan'];
const LIGHT_VALUES = ['1', '2', '3', '4', '5', '6', '7', '8', '9', 'Skip', 'Reverse', 'Draw One', 'Flip'];
const DARK_VALUES = ['1', '2', '3', '4', '5', '6', '7', '8', '9', 'Skip', 'Reverse', 'Draw Five', 'Flip'];

// Utility Functions
function shuffle(arr) {
  const array = [...arr];
  for (let i = array.length - 1; i > 0; i--) {
    const j = Math.floor(Math.random() * (i + 1));
    [array[i], array[j]] = [array[j], array[i]];
  }
  return array;
}

function buildDeck() {
  const deck = [];
  for (let i = 0; i < LIGHT_COLORS.length; i++) {
    for (let j = 0; j < LIGHT_VALUES.length; j++) {
      deck.push({
        id: `${i}-${j}-${Math.random()}`,
        lightColor: LIGHT_COLORS[i],
        lightValue: LIGHT_VALUES[j],
        darkColor: DARK_COLORS[i],
        darkValue: DARK_VALUES[j]
      });
    }
  }
  for (let i = 0; i < 4; i++) {
    deck.push({
      id: `wild-${i}-${Math.random()}`,
      lightColor: 'Wild',
      lightValue: 'Wild',
      darkColor: 'Wild Dark',
      darkValue: 'Wild Dark'
    });
    deck.push({
      id: `wild-draw-${i}-${Math.random()}`,
      lightColor: 'Wild',
      lightValue: 'Wild Draw 2',
      darkColor: 'Wild Dark',
      darkValue: 'Wild Draw Color'
    });
  }
  return shuffle(deck);
}

function canPlay(card, topCard, activeColor, side) {
  if (!card || !topCard) return false;
  const cardColor = side === 'light' ? card.lightColor : card.darkColor;
  const topColor = side === 'light' ? topCard.lightColor : topCard.darkColor;
  const cardValue = side === 'light' ? card.lightValue : card.darkValue;
  const topValue = side === 'light' ? topCard.lightValue : topCard.darkValue;
  if (cardColor.includes('Wild')) return true;
  const matchColor = activeColor || topColor;
  return cardColor === matchColor || cardValue === topValue;
}

function generateRoomCode() {
  return Math.random().toString(36).substring(2, 8).toUpperCase();
}

function App() {
  // Screen state
  const [screen, setScreen] = useState('menu');
  
  // Player state
  const [playerName, setPlayerName] = useState('');
  const [playerId, setPlayerId] = useState('');
  
  // Room state
  const [roomCode, setRoomCode] = useState('');
  const [isHost, setIsHost] = useState(false);
  const [roomRef, setRoomRef] = useState(null);
  
  // Game state from Firebase
  const [gameState, setGameState] = useState(null);
  const [myHand, setMyHand] = useState([]);
  
  // Local UI state
  const [message, setMessage] = useState('');
  const [needsColorPick, setNeedsColorPick] = useState(false);
  
  // Initialize player ID on mount
  useEffect(() => {
    const id = 'player_' + Math.random().toString(36).substring(2, 15);
    setPlayerId(id);
  }, []);
  
  // Listen to game state changes
  useEffect(() => {
    if (!roomRef) return;
    
    const unsubscribe = onValue(roomRef, (snapshot) => {
      const data = snapshot.val();
      if (data) {
        setGameState(data);
        
        // Update my hand
        const player = data.players?.find(p => p.id === playerId);
        if (player) {
          setMyHand(player.cards || []);
        }
        
        // Update message
        if (data.winner) {
          setMessage(`🎉 ${data.winner} WINS! 🎉`);
        } else if (data.currentPlayerId === playerId) {
          setMessage("Your turn!");
        } else {
          const currentPlayer = data.players?.find(p => p.id === data.currentPlayerId);
          setMessage(`${currentPlayer?.name || 'Player'}'s turn`);
        }
      } else {
        // Room was deleted
        setScreen('menu');
        setMessage('Room was closed');
      }
    });
    
    return () => off(roomRef);
  }, [roomRef, playerId]);
  
  // Create a new room
  const createRoom = () => {
    if (!playerName.trim()) {
      alert('Please enter your name!');
      return;
    }
    
    const code = generateRoomCode();
    const newRoomRef = ref(database, `rooms/${code}`);
    
    const initialData = {
      code,
      hostId: playerId,
      status: 'waiting',
      players: [{
        id: playerId,
        name: playerName,
        cards: [],
        isHost: true
      }],
      createdAt: Date.now()
    };
    
    set(newRoomRef, initialData).then(() => {
      setRoomCode(code);
      setRoomRef(newRoomRef);
      setIsHost(true);
      setScreen('lobby');
    });
  };
  
  // Join existing room
  const joinRoom = (code) => {
    if (!playerName.trim()) {
      alert('Please enter your name!');
      return;
    }
    
    const joinRoomRef = ref(database, `rooms/${code}`);
    
    onValue(joinRoomRef, (snapshot) => {
      const data = snapshot.val();
      if (!data) {
        alert('Room not found!');
        return;
      }
      
      if (data.status === 'playing') {
        alert('Game already in progress!');
        return;
      }
      
      if (data.players.length >= 6) {
        alert('Room is full!');
        return;
      }
      
      // Check if already in room
      const alreadyJoined = data.players.some(p => p.id === playerId);
      
      if (!alreadyJoined) {
        const updatedPlayers = [...data.players, {
          id: playerId,
          name: playerName,
          cards: [],
          isHost: false
        }];
        
        update(joinRoomRef, { players: updatedPlayers });
      }
      
      setRoomCode(code);
      setRoomRef(joinRoomRef);
      setIsHost(false);
      setScreen('lobby');
    }, { onlyOnce: true });
  };
  
  // Start game (host only)
  const startGame = () => {
    if (!isHost || !gameState) return;
    
    if (gameState.players.length < 2) {
      alert('Need at least 2 players to start!');
      return;
    }
    
    const deck = buildDeck();
    const players = gameState.players.map((player, index) => ({
      ...player,
      cards: deck.slice(index * 7, (index + 1) * 7)
    }));
    
    const deckStart = players.length * 7;
    
    const gameData = {
      ...gameState,
      status: 'playing',
      players,
      deck: deck.slice(deckStart + 1),
      discard: [deck[deckStart]],
      currentPlayerId: players[0].id,
      side: 'light',
      direction: 1,
      activeColor: null,
      winner: null
    };
    
    update(roomRef, gameData);
    setScreen('game');
  };
  
  // Play a card
  const playCard = (cardIndex) => {
    if (!gameState || gameState.currentPlayerId !== playerId || gameState.winner) return;
    
    const card = myHand[cardIndex];
    const topCard = gameState.discard[gameState.discard.length - 1];
    
    if (!canPlay(card, topCard, gameState.activeColor, gameState.side)) {
      return;
    }
    
    const cardColor = gameState.side === 'light' ? card.lightColor : card.darkColor;
    const cardValue = gameState.side === 'light' ? card.lightValue : card.darkValue;
    
    // Update player's hand
    const updatedPlayers = gameState.players.map(p => {
      if (p.id === playerId) {
        return { ...p, cards: p.cards.filter((_, i) => i !== cardIndex) };
      }
      return p;
    });
    
    // Add to discard
    const updatedDiscard = [...gameState.discard, card];
    
    // Check for wild card
    if (cardColor.includes('Wild')) {
      setNeedsColorPick(true);
      update(roomRef, {
        players: updatedPlayers,
        discard: updatedDiscard,
        pendingWildCard: true
      });
      return;
    }
    
    // Apply card effects and determine next player
    const result = applyCardEffect(card, updatedPlayers, gameState);
    
    update(roomRef, {
      players: result.players,
      discard: updatedDiscard,
      deck: result.deck,
      currentPlayerId: result.nextPlayerId,
      side: result.side,
      winner: result.winner,
      activeColor: null
    });
  };
  
  // Apply card effects
  const applyCardEffect = (card, players, state) => {
    const cardValue = state.side === 'light' ? card.lightValue : card.darkValue;
    let currentIndex = players.findIndex(p => p.id === state.currentPlayerId);
    let nextIndex = (currentIndex + state.direction + players.length) % players.length;
    let updatedPlayers = [...players];
    let updatedDeck = [...state.deck];
    let newSide = state.side;
    let winner = null;
    
    // Draw cards function
    const drawCardsForPlayer = (playerIndex, count) => {
      for (let i = 0; i < count; i++) {
        if (updatedDeck.length === 0) {
          const topCard = state.discard[state.discard.length - 1];
          updatedDeck = shuffle(state.discard.slice(0, -1));
        }
        if (updatedDeck.length > 0) {
          updatedPlayers[playerIndex].cards.push(updatedDeck.shift());
        }
      }
    };
    
    switch (cardValue) {
      case 'Skip':
        nextIndex = (nextIndex + state.direction + players.length) % players.length;
        break;
        
      case 'Reverse':
        const newDirection = state.direction * -1;
        if (players.length === 2) {
          nextIndex = (nextIndex + newDirection + players.length) % players.length;
        }
        update(roomRef, { direction: newDirection });
        break;
        
      case 'Draw One':
        drawCardsForPlayer(nextIndex, 1);
        nextIndex = (nextIndex + state.direction + players.length) % players.length;
        break;
        
      case 'Draw Five':
        drawCardsForPlayer(nextIndex, 5);
        nextIndex = (nextIndex + state.direction + players.length) % players.length;
        break;
        
      case 'Wild Draw 2':
        drawCardsForPlayer(nextIndex, 2);
        nextIndex = (nextIndex + state.direction + players.length) % players.length;
        break;
        
      case 'Wild Draw Color':
        let drawn = 0;
        const targetColor = state.activeColor;
        while (drawn < 10 && updatedDeck.length > 0) {
          const beforeLength = updatedPlayers[nextIndex].cards.length;
          drawCardsForPlayer(nextIndex, 1);
          const lastCard = updatedPlayers[nextIndex].cards[updatedPlayers[nextIndex].cards.length - 1];
          const lastCardColor = state.side === 'light' ? lastCard.lightColor : lastCard.darkColor;
          drawn++;
          if (lastCardColor === targetColor) break;
        }
        nextIndex = (nextIndex + state.direction + players.length) % players.length;
        break;
        
      case 'Flip':
        newSide = state.side === 'light' ? 'dark' : 'light';
        break;
    }
    
    // Check for winner
    if (updatedPlayers[currentIndex].cards.length === 0) {
      winner = updatedPlayers[currentIndex].name;
    }
    
    return {
      players: updatedPlayers,
      deck: updatedDeck,
      nextPlayerId: players[nextIndex].id,
      side: newSide,
      winner
    };
  };
  
  // Select color for wild card
  const selectColor = (color) => {
    if (!needsColorPick) return;
    
    setNeedsColorPick(false);
    
    const currentIndex = gameState.players.findIndex(p => p.id === playerId);
    const nextIndex = (currentIndex + gameState.direction + gameState.players.length) % gameState.players.length;
    
    update(roomRef, {
      activeColor: color,
      currentPlayerId: gameState.players[nextIndex].id,
      pendingWildCard: false
    });
  };
  
  // Draw a card
  const drawCard = () => {
    if (!gameState || gameState.currentPlayerId !== playerId || gameState.winner || needsColorPick) return;
    
    if (gameState.deck.length === 0) {
      const topCard = gameState.discard[gameState.discard.length - 1];
      const reshuffled = shuffle(gameState.discard.slice(0, -1));
      update(roomRef, { deck: reshuffled, discard: [topCard] });
    }
    
    const card = gameState.deck[0];
    const updatedDeck = gameState.deck.slice(1);
    
    const updatedPlayers = gameState.players.map(p => {
      if (p.id === playerId) {
        return { ...p, cards: [...p.cards, card] };
      }
      return p;
    });
    
    const currentIndex = gameState.players.findIndex(p => p.id === playerId);
    const nextIndex = (currentIndex + gameState.direction + gameState.players.length) % gameState.players.length;
    
    update(roomRef, {
      players: updatedPlayers,
      deck: updatedDeck,
      currentPlayerId: gameState.players[nextIndex].id
    });
  };
  
  // Leave room
  const leaveRoom = () => {
    if (roomRef && gameState) {
      if (isHost) {
        // Delete room if host leaves
        remove(roomRef);
      } else {
        // Remove player from room
        const updatedPlayers = gameState.players.filter(p => p.id !== playerId);
        update(roomRef, { players: updatedPlayers });
      }
    }
    setScreen('menu');
    setRoomRef(null);
    setGameState(null);
    setMyHand([]);
  };
  
  // Get card color class
  const getCardClass = (color) => {
    const colorMap = {
      'Red': 'card-red', 'Green': 'card-green', 'Blue': 'card-blue',
      'Yellow': 'card-yellow', 'Pink': 'card-pink', 'Teal': 'card-teal',
      'Purple': 'card-purple', 'Orange': 'card-orange', 'Cyan': 'card-cyan',
      'Wild': 'card-wild', 'Wild Dark': 'card-wild'
    };
    return colorMap[color] || 'card-wild';
  };
  
  // RENDER MENU SCREEN
  if (screen === 'menu') {
    return (
      <div className="app">
        <div className="stars"></div>
        <div className="container">
          <div className="menu-header">
            <h1 className="game-title">UNO FLIP</h1>
            <p className="game-subtitle">Online Multiplayer Edition</p>
          </div>
          
          <div className="online-menu">
            <div className="name-section">
              <label className="input-label">Your Name</label>
              <input
                type="text"
                className="name-input-main"
                placeholder="Enter your name"
                value={playerName}
                onChange={(e) => setPlayerName(e.target.value)}
                maxLength={20}
              />
            </div>
            
            <div className="menu-cards">
              <div className="menu-card" onClick={createRoom}>
                <div className="menu-card-icon">🎮</div>
                <h2>Create Room</h2>
                <p>Host a new game</p>
              </div>
              
              <div className="menu-card" onClick={() => setScreen('join')}>
                <div className="menu-card-icon">🔗</div>
                <h2>Join Room</h2>
                <p>Enter room code</p>
              </div>
              
              <div className="menu-card" onClick={() => setScreen('rules')}>
                <div className="menu-card-icon">📖</div>
                <h2>How to Play</h2>
                <p>Learn the rules</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
  
  // RENDER JOIN SCREEN
  if (screen === 'join') {
    return (
      <div className="app">
        <div className="stars"></div>
        <div className="container">
          <button className="back-button" onClick={() => setScreen('menu')}>← Back</button>
          
          <div className="join-container">
            <h1 className="join-title">Join Game</h1>
            <p className="join-subtitle">Enter the room code shared by your friend</p>
            
            <input
              type="text"
              className="room-code-input"
              placeholder="ROOM CODE"
              value={roomCode}
              onChange={(e) => setRoomCode(e.target.value.toUpperCase())}
              maxLength={6}
            />
            
            <button className="join-button" onClick={() => joinRoom(roomCode)}>
              Join Room 🚀
            </button>
          </div>
        </div>
      </div>
    );
  }
  
  // RENDER LOBBY SCREEN
  if (screen === 'lobby') {
    return (
      <div className="app">
        <div className="stars"></div>
        <div className="container">
          <button className="back-button" onClick={leaveRoom}>← Leave</button>
          
          <div className="lobby-container">
            <h1 className="lobby-title">Game Lobby</h1>
            
            <div className="room-code-display">
              <span className="code-label">Room Code:</span>
              <span className="code-value">{roomCode}</span>
              <button 
                className="copy-button"
                onClick={() => {
                  navigator.clipboard.writeText(roomCode);
                  alert('Room code copied!');
                }}
              >
                📋 Copy
              </button>
            </div>
            
            <div className="players-waiting">
              <h2 className="section-title">Players ({gameState?.players?.length || 0}/6)</h2>
              <div className="player-list">
                {gameState?.players?.map((player, index) => (
                  <div key={player.id} className="lobby-player">
                    <span className="player-number">{index + 1}</span>
                    <span className="lobby-player-name">{player.name}</span>
                    {player.isHost && <span className="host-badge">👑 Host</span>}
                  </div>
                ))}
              </div>
            </div>
            
            {isHost && (
              <button className="start-button" onClick={startGame}>
                Start Game 🎮
              </button>
            )}
            
            {!isHost && (
              <div className="waiting-message">
                Waiting for host to start the game...
              </div>
            )}
          </div>
        </div>
      </div>
    );
  }
  
  // RENDER RULES SCREEN
  if (screen === 'rules') {
    return (
      <div className="app">
        <div className="stars"></div>
        <div className="container">
          <button className="back-button" onClick={() => setScreen('menu')}>← Back</button>
          
          <div className="rules-container">
            <h1 className="rules-title">UNO FLIP Rules</h1>
            
            <div className="rules-section">
              <h2 className="rules-heading">🎯 Objective</h2>
              <p>Be the first player to get rid of all your cards by matching them to the top card on the discard pile by color or number.</p>
            </div>
            
            <div className="rules-section">
              <h2 className="rules-heading">🔄 The FLIP Mechanic</h2>
              <p>UNO FLIP features double-sided cards with a <strong>Light Side</strong> and a <strong>Dark Side</strong>. When a FLIP card is played, ALL cards in the game flip to the opposite side!</p>
            </div>
            
            <div className="rules-section">
              <h2 className="rules-heading">☀️ Light Side Cards</h2>
              <ul>
                <li><strong>Skip:</strong> Next player loses their turn</li>
                <li><strong>Reverse:</strong> Changes direction of play</li>
                <li><strong>Draw One:</strong> Next player draws 1 card</li>
                <li><strong>Flip:</strong> Flip to Dark side</li>
                <li><strong>Wild:</strong> Choose any color</li>
                <li><strong>Wild Draw 2:</strong> Choose color, next draws 2</li>
              </ul>
            </div>
            
            <div className="rules-section">
              <h2 className="rules-heading">🌙 Dark Side Cards</h2>
              <ul>
                <li><strong>Skip:</strong> Next player loses their turn</li>
                <li><strong>Reverse:</strong> Changes direction of play</li>
                <li><strong>Draw Five:</strong> Next player draws 5 cards!</li>
                <li><strong>Flip:</strong> Flip back to Light side</li>
                <li><strong>Wild Dark:</strong> Choose any color</li>
                <li><strong>Wild Draw Color:</strong> Draw until color match!</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    );
  }
  
  // RENDER GAME SCREEN
  if (!gameState) return <div>Loading...</div>;
  
  const topCard = gameState.discard?.[gameState.discard.length - 1];
  const currentTopColor = gameState.side === 'light' ? topCard?.lightColor : topCard?.darkColor;
  const currentTopValue = gameState.side === 'light' ? topCard?.lightValue : topCard?.darkValue;
  const isMyTurn = gameState.currentPlayerId === playerId;
  
  return (
    <div className="app">
      <div className="stars"></div>
      <div className="game-container">
        <div className="game-header">
          <button className="header-button" onClick={leaveRoom}>🏠 Leave</button>
          <div className="side-indicator">
            {gameState.side === 'light' ? '☀️ LIGHT SIDE' : '🌙 DARK SIDE'}
          </div>
          <div className="room-info-header">Room: {roomCode}</div>
        </div>
        
        <div className="message-box">{message}</div>
        
        <div className="game-board">
          {/* Other Players */}
          <div className="other-players">
            {gameState.players?.map((player) => {
              if (player.id === playerId) return null;
              return (
                <div key={player.id} className={`player-info ${player.id === gameState.currentPlayerId ? 'active' : ''}`}>
                  <div className="player-header">
                    <span className="player-name">{player.name}</span>
                    {player.id === gameState.currentPlayerId && <span className="turn-indicator">●</span>}
                  </div>
                  <div className="opponent-cards">
                    {player.cards?.map((card, i) => (
                      <div key={i} className="card-back">UNO</div>
                    ))}
                  </div>
                  <div className="card-count">{player.cards?.length || 0} cards</div>
                </div>
              );
            })}
          </div>
          
          {/* Center Play Area */}
          <div className="play-area">
            <div className="deck-area">
              <div className="deck-card" onClick={drawCard}>
                <div className="deck-label">DRAW</div>
                <div className="deck-count">{gameState.deck?.length || 0}</div>
              </div>
              
              {topCard && (
                <div className={`game-card ${getCardClass(currentTopColor)}`}>
                  <div className="card-top">{currentTopColor}</div>
                  <div className="card-center">{currentTopValue}</div>
                  <div className="card-bottom">
                    {gameState.activeColor ? `Color: ${gameState.activeColor}` : currentTopColor}
                  </div>
                </div>
              )}
            </div>
            
            {needsColorPick && (
              <div className="color-picker">
                <div className="picker-title">Choose a Color</div>
                <div className="color-options">
                  {(gameState.side === 'light' ? LIGHT_COLORS : DARK_COLORS).map(color => (
                    <button
                      key={color}
                      className={`color-button ${getCardClass(color)}`}
                      onClick={() => selectColor(color)}
                    >
                      {color}
                    </button>
                  ))}
                </div>
              </div>
            )}
          </div>
          
          {/* Your Hand */}
          <div className="your-hand">
            <div className={`player-info your-info ${isMyTurn ? 'active' : ''}`}>
              <div className="player-header">
                <span className="player-name">You ({playerName})</span>
                {isMyTurn && <span className="turn-indicator">●</span>}
              </div>
            </div>
            
            <div className="hand-cards">
              {myHand.map((card, index) => {
                const cardColor = gameState.side === 'light' ? card.lightColor : card.darkColor;
                const cardValue = gameState.side === 'light' ? card.lightValue : card.darkValue;
                const playable = canPlay(card, topCard, gameState.activeColor, gameState.side) && 
                               isMyTurn && 
                               !needsColorPick && 
                               !gameState.winner;
                
                return (
                  <button
                    key={card.id}
                    className={`game-card ${getCardClass(cardColor)} ${playable ? 'playable' : ''}`}
                    onClick={() => playable && playCard(index)}
                    disabled={!playable}
                  >
                    <div className="card-top">{cardColor}</div>
                    <div className="card-center">{cardValue}</div>
                    <div className="card-bottom">{cardColor}</div>
                  </button>
                );
              })}
            </div>
          </div>
        </div>
      </div>
      
      {/* Winner Modal */}
      {gameState.winner && (
        <div className="winner-overlay">
          <div className="winner-modal">
            <h1 className="winner-title">🎉 WINNER! 🎉</h1>
            <p className="winner-name">{gameState.winner}</p>
            <button className="winner-button" onClick={leaveRoom}>
              Back to Menu
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

export default App;