# Modern Tic Tac Toe with AI

A beautiful, feature-rich Tic Tac Toe game for Android with intelligent AI opponents and stunning glassmorphism UI design.

## Features

### Game Modes

- **Two Player Mode**: Classic human vs human gameplay
- **AI Mode**: Challenge intelligent computer opponents


### AI Difficulty Levels

- **Easy**: 70% random moves, 30% strategic (perfect for beginners)
- **Medium**: 30% random moves, 70% strategic (balanced challenge)
- **Hard**: 100% Minimax algorithm (nearly unbeatable)


### Modern UI Design

- **Glassmorphism Effects**: Stunning transparent card designs
- **Custom Typography**: Beautiful font combinations
- **Smooth Animations**: Button presses, winning celebrations, transitions
- **Responsive Design**: Optimized for all screen sizes
- **Visual Feedback**: Dynamic button states and highlights


### Interactive Features

- **Haptic Feedback**: Vibration on moves and victories
- **Score Tracking**: Persistent win/loss counters
- **Smart Status Updates**: Context-aware game messages
- **Winning Animations**: Highlighted winning combinations
- **AI Thinking Simulation**: Realistic processing delays


## AI Algorithms

### Easy Mode

```plaintext
Strategy: Random + Basic Logic
- 70% random move selection
- 30% strategic moves (win/block)
- Perfect for learning
```

### Medium Mode

```plaintext
Strategy: Balanced Intelligence
- 30% random moves for unpredictability
- 70% strategic logic
- Priority: Win → Block → Center → Corners
```

### Hard Mode

```plaintext
Strategy: Minimax Algorithm
- Game theory implementation
- Calculates all possible future moves
- Always chooses optimal move
- Nearly impossible to beat
```

## Screenshots

*Add your app screenshots here*

## ️ Technical Details

### Built With

- **Language**: Java
- **Platform**: Android (API 21+)
- **UI Framework**: Native Android Views
- **Animations**: ObjectAnimator & AnimatorSet
- **Architecture**: Single Activity with modular AI class


### Key Components

- `MainActivity.java` - Main game logic and UI handling
- `AIPlayer.java` - AI intelligence and algorithms
- Custom drawable resources for glassmorphism effects
- Responsive XML layouts with custom fonts


### Algorithms Implemented

- **Minimax Algorithm**: For unbeatable AI gameplay
- **Strategic Logic**: Priority-based move selection
- **Random Selection**: For beginner-friendly AI
- **Win Detection**: Efficient pattern matching


## Installation

1. Clone the repository


```shellscript
git clone https://github.com/yourusername/modern-tictactoe.git
```

2. Open in Android Studio
3. Build and run on your Android device


### Requirements

- Android Studio 4.0+
- Android SDK 21+
- Java 8+


## How to Play

1. **Select Game Mode**: Choose between 2 Players or VS AI
2. **Choose Difficulty** (AI Mode): Pick Easy, Medium, or Hard
3. **Make Your Move**: Tap any empty cell to place your mark
4. **Win the Game**: Get three in a row (horizontal, vertical, or diagonal)
5. **Track Progress**: View your wins and losses in the score display


## Game Features

### Gameplay

- ✅ Classic 3x3 Tic Tac Toe grid
- ✅ Turn-based gameplay
- ✅ Win detection for all patterns
- ✅ Draw game handling
- ✅ Game reset and new game options


### AI Intelligence

- ✅ Three difficulty levels
- ✅ Smart move prioritization
- ✅ Minimax algorithm implementation
- ✅ Realistic thinking delays
- ✅ Unbeatable hard mode


### User Experience

- ✅ Intuitive touch controls
- ✅ Visual feedback for all actions
- ✅ Smooth animations throughout
- ✅ Haptic feedback support
- ✅ Portrait orientation optimized


## Customization

### Fonts

Replace fonts in `res/font/`:

- `font_3.ttf` - Main titles and scores
- `font_ghar.ttf` - UI text and buttons


### Colors

Modify colors in `res/values/colors.xml`:

- Player X color: `#FF6B6B`
- Player O color: `#4ECDC4`
- Background gradients and glass effects


### Difficulty Tuning

Adjust AI behavior in `AIPlayer.java`:

- Change random vs strategic ratios
- Modify thinking delays
- Customize move priorities


## Performance

- **Smooth 60fps** animations
- **Instant response** to user input
- **Efficient algorithms** with minimal battery usage
- **Memory optimized** for all Android devices
- **Fast AI calculations** even on older devices


## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request


## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## ‍ Author

**Your Name**

- Email: [mohammedhayatazad13@gmail.com]


## Acknowledgments

- Minimax algorithm implementation inspired by game theory principles
- Glassmorphism design trends
- Android Material Design guidelines
- Open source community for inspiration


## Future Enhancements

- Online multiplayer support
- Tournament mode
- Custom board sizes (4x4, 5x5)
- Sound effects and background music
- Dark/Light theme toggle
- Statistics and achievements
- Multiple AI personalities

