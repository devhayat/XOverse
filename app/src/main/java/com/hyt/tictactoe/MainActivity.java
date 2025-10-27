package com.hyt.tictactoe;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    // UI Components
    private Button[][] buttons = new Button[3][3];
    private TextView textStatus, textPlayerX, textPlayerO;
    private Button resetButton, newGameButton;
    private Button twoPlayerButton, aiPlayerButton;
    private Button easyButton, mediumButton, hardButton;
    private LinearLayout difficultyLayout;

    // Game State
    private boolean isXTurn = true;
    private int roundCount = 0;
    private int xScore = 0, oScore = 0;
    private boolean gameActive = true;
    private int[] winningLine = new int[3];

    // AI Components
    private AIPlayer aiPlayer;
    private boolean isAIMode = false;
    private boolean isAITurn = false;
    private AIPlayer.Difficulty currentDifficulty = AIPlayer.Difficulty.EASY;

    // Animation and Effects
    private Vibrator vibrator;
    private MediaPlayer clickSound, winSound;
    private Handler animationHandler = new Handler();
    private Handler aiHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        initializeGame();
        setupSoundEffects();
        setupAnimations();
        initializeAI();
    }

    private void initializeViews() {
        textStatus = findViewById(R.id.statusText);
        textPlayerX = findViewById(R.id.playerXScore);
        textPlayerO = findViewById(R.id.playerOScore);
        resetButton = findViewById(R.id.resetButton);
        newGameButton = findViewById(R.id.newGameButton);

        // Mode selection buttons
        twoPlayerButton = findViewById(R.id.twoPlayerButton);
        aiPlayerButton = findViewById(R.id.aiPlayerButton);
        difficultyLayout = findViewById(R.id.difficultyLayout);

        // Difficulty buttons
        easyButton = findViewById(R.id.easyButton);
        mediumButton = findViewById(R.id.mediumButton);
        hardButton = findViewById(R.id.hardButton);

        // Initialize game buttons
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String btnID = "button" + i + j;
                int resID = getResources().getIdentifier(btnID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);

                final int row = i;
                final int col = j;
                buttons[i][j].setOnClickListener(v -> handleMove(buttons[row][col], row, col));
            }
        }

        resetButton.setOnClickListener(v -> resetBoard());
        newGameButton.setOnClickListener(v -> newGame());

        // Mode selection listeners
        twoPlayerButton.setOnClickListener(v -> setTwoPlayerMode());
        aiPlayerButton.setOnClickListener(v -> setAIMode());

        // Difficulty selection listeners
        easyButton.setOnClickListener(v -> setDifficulty(AIPlayer.Difficulty.EASY));
        mediumButton.setOnClickListener(v -> setDifficulty(AIPlayer.Difficulty.MEDIUM));
        hardButton.setOnClickListener(v -> setDifficulty(AIPlayer.Difficulty.HARD));

        // Initialize vibrator
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    private void initializeGame() {
        updateScoreDisplay();
        updateStatusText();
        animateStatusText();
    }

    private void initializeAI() {
        aiPlayer = new AIPlayer(currentDifficulty);
    }

    private void setTwoPlayerMode() {
        isAIMode = false;
        updateModeButtons();
        difficultyLayout.setVisibility(View.GONE);
        resetBoard();
        updateStatusText();
    }

    private void setAIMode() {
        isAIMode = true;
        updateModeButtons();
        difficultyLayout.setVisibility(View.VISIBLE);
        resetBoard();
        updateStatusText();
    }

    private void setDifficulty(AIPlayer.Difficulty difficulty) {
        currentDifficulty = difficulty;
        aiPlayer.setDifficulty(difficulty);
        updateDifficultyButtons();
        resetBoard();
    }

    private void updateModeButtons() {
        if (isAIMode) {
            twoPlayerButton.setBackground(ContextCompat.getDrawable(this, R.drawable.mode_button_unselected));
            aiPlayerButton.setBackground(ContextCompat.getDrawable(this, R.drawable.mode_button_selected));
        } else {
            twoPlayerButton.setBackground(ContextCompat.getDrawable(this, R.drawable.mode_button_selected));
            aiPlayerButton.setBackground(ContextCompat.getDrawable(this, R.drawable.mode_button_unselected));
        }
    }

    private void updateDifficultyButtons() {
        // Reset all buttons
        easyButton.setBackground(ContextCompat.getDrawable(this, R.drawable.difficulty_button_unselected));
        mediumButton.setBackground(ContextCompat.getDrawable(this, R.drawable.difficulty_button_unselected));
        hardButton.setBackground(ContextCompat.getDrawable(this, R.drawable.difficulty_button_unselected));

        // Highlight selected difficulty
        switch (currentDifficulty) {
            case EASY:
                easyButton.setBackground(ContextCompat.getDrawable(this, R.drawable.difficulty_button_selected));
                break;
            case MEDIUM:
                mediumButton.setBackground(ContextCompat.getDrawable(this, R.drawable.difficulty_button_selected));
                break;
            case HARD:
                hardButton.setBackground(ContextCompat.getDrawable(this, R.drawable.difficulty_button_selected));
                break;
        }
    }

    private void setupSoundEffects() {
        try {
            // You can add sound files to res/raw/ folder
            // clickSound = MediaPlayer.create(this, R.raw.click_sound);
            // winSound = MediaPlayer.create(this, R.raw.win_sound);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupAnimations() {
        // Animate buttons on startup
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final Button button = buttons[i][j];
                button.setAlpha(0f);
                button.setScaleX(0.5f);
                button.setScaleY(0.5f);

                animationHandler.postDelayed(() -> {
                    ObjectAnimator fadeIn = ObjectAnimator.ofFloat(button, "alpha", 0f, 1f);
                    ObjectAnimator scaleX = ObjectAnimator.ofFloat(button, "scaleX", 0.5f, 1f);
                    ObjectAnimator scaleY = ObjectAnimator.ofFloat(button, "scaleY", 0.5f, 1f);

                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(fadeIn, scaleX, scaleY);
                    animatorSet.setDuration(300);
                    animatorSet.setInterpolator(new BounceInterpolator());
                    animatorSet.start();
                }, (i * 3 + j) * 100);
            }
        }
    }

    private void handleMove(Button btn, int row, int col) {
        if (!gameActive || !btn.getText().toString().equals("") || isAITurn) {
            return;
        }

        makeMove(btn, row, col);

        // If it's AI mode and game is still active, let AI make a move
        if (isAIMode && gameActive && !isXTurn) {
            makeAIMove();
        }
    }

    private void makeMove(Button btn, int row, int col) {
        // Play click sound
        playClickSound();

        // Vibrate
        if (vibrator != null) {
            vibrator.vibrate(50);
        }

        // Animate button press
        animateButtonPress(btn);

        // Set button text and color
        String currentPlayer = isXTurn ? "X" : "O";
        btn.setText(currentPlayer);

        if (isXTurn) {
            btn.setTextColor(ContextCompat.getColor(this, R.color.player_x_color));
        } else {
            btn.setTextColor(ContextCompat.getColor(this, R.color.player_o_color));
        }

        roundCount++;

        // Check for winner
        if (checkWinner()) {
            handleGameWin();
        } else if (roundCount == 9) {
            handleGameDraw();
        } else {
            // Switch turns
            isXTurn = !isXTurn;
            updateStatusText();
            animateStatusText();
        }
    }

    private void makeAIMove() {
        if (!gameActive || isXTurn) return;

        isAITurn = true;
        textStatus.setText(getString(R.string.ai_thinking));
        animateStatusText();

        // Add delay to make AI seem like it's thinking
        int delay = currentDifficulty == AIPlayer.Difficulty.HARD ? 1500 :
                currentDifficulty == AIPlayer.Difficulty.MEDIUM ? 1000 : 500;

        aiHandler.postDelayed(() -> {
            String[][] board = getBoardState();
            int[] aiMove = aiPlayer.getBestMove(board);

            if (aiMove != null && gameActive) {
                Button aiButton = buttons[aiMove[0]][aiMove[1]];
                makeMove(aiButton, aiMove[0], aiMove[1]);
            }

            isAITurn = false;
        }, delay);
    }

    private String[][] getBoardState() {
        String[][] board = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = buttons[i][j].getText().toString();
            }
        }
        return board;
    }

    private void animateButtonPress(Button button) {
        ObjectAnimator scaleDown = ObjectAnimator.ofFloat(button, "scaleX", 1f, 0.9f);
        ObjectAnimator scaleUp = ObjectAnimator.ofFloat(button, "scaleX", 0.9f, 1f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(button, "scaleY", 1f, 0.9f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(button, "scaleY", 0.9f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleDown).with(scaleDownY);
        animatorSet.play(scaleUp).with(scaleUpY).after(scaleDown);
        animatorSet.setDuration(100);
        animatorSet.start();
    }

    private void handleGameWin() {
        gameActive = false;
        isAITurn = false;

        if (isXTurn) {
            xScore++;
            if (isAIMode) {
                textStatus.setText(getString(R.string.you_win));
            } else {
                textStatus.setText(getString(R.string.player_x_wins));
            }
        } else {
            oScore++;
            if (isAIMode) {
                textStatus.setText(getString(R.string.ai_wins));
            } else {
                textStatus.setText(getString(R.string.player_o_wins));
            }
        }

        updateScoreDisplay();
        animateWinningButtons();
        playWinSound();

        if (vibrator != null) {
            vibrator.vibrate(new long[]{0, 100, 100, 100}, -1);
        }

        disableAllButtons();
        showWinDialog();
    }

    private void handleGameDraw() {
        gameActive = false;
        isAITurn = false;
        textStatus.setText(getString(R.string.game_draw));
        animateStatusText();
        disableAllButtons();
        showDrawDialog();
    }

    private void animateWinningButtons() {
        for (int pos : winningLine) {
            int row = pos / 3;
            int col = pos % 3;
            Button button = buttons[row][col];

            // Change background to highlight winning combination
            button.setBackground(ContextCompat.getDrawable(this, R.drawable.winner_button_background));

            // Animate winning buttons with individual ObjectAnimators
            ObjectAnimator pulseX = ObjectAnimator.ofFloat(button, "scaleX", 1f, 1.2f, 1f);
            pulseX.setDuration(500);
            pulseX.setRepeatCount(2);
            pulseX.setInterpolator(new AccelerateDecelerateInterpolator());

            ObjectAnimator pulseY = ObjectAnimator.ofFloat(button, "scaleY", 1f, 1.2f, 1f);
            pulseY.setDuration(500);
            pulseY.setRepeatCount(2);
            pulseY.setInterpolator(new AccelerateDecelerateInterpolator());

            // Start both animations
            pulseX.start();
            pulseY.start();
        }
    }

    private void animateStatusText() {
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(textStatus, "alpha", 1f, 0f);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(textStatus, "alpha", 0f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(textStatus, "scaleX", 0.8f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(textStatus, "scaleY", 0.8f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(fadeIn).with(scaleX).with(scaleY).after(fadeOut);
        animatorSet.setDuration(200);
        animatorSet.start();
    }

    private boolean checkWinner() {
        String[][] board = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = buttons[i][j].getText().toString();
            }
        }

        // Check rows
        for (int i = 0; i < 3; i++) {
            if (!board[i][0].isEmpty() &&
                    board[i][0].equals(board[i][1]) &&
                    board[i][0].equals(board[i][2])) {
                winningLine = new int[]{i * 3, i * 3 + 1, i * 3 + 2};
                return true;
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {
            if (!board[0][i].isEmpty() &&
                    board[0][i].equals(board[1][i]) &&
                    board[0][i].equals(board[2][i])) {
                winningLine = new int[]{i, i + 3, i + 6};
                return true;
            }
        }

        // Check diagonals
        if (!board[0][0].isEmpty() &&
                board[0][0].equals(board[1][1]) &&
                board[0][0].equals(board[2][2])) {
            winningLine = new int[]{0, 4, 8};
            return true;
        }

        if (!board[0][2].isEmpty() &&
                board[0][2].equals(board[1][1]) &&
                board[0][2].equals(board[2][0])) {
            winningLine = new int[]{2, 4, 6};
            return true;
        }

        return false;
    }

    private void disableAllButtons() {
        for (Button[] row : buttons) {
            for (Button btn : row) {
                btn.setEnabled(false);
            }
        }
    }

    private void enableAllButtons() {
        for (Button[] row : buttons) {
            for (Button btn : row) {
                btn.setEnabled(true);
            }
        }
    }

    private void resetBoard() {
        for (Button[] row : buttons) {
            for (Button btn : row) {
                btn.setText("");
                btn.setEnabled(true);
                btn.setBackground(ContextCompat.getDrawable(this, R.drawable.game_button_background));
                btn.setTextColor(ContextCompat.getColor(this, R.color.white));

                // Reset animation
                ObjectAnimator resetAnim = ObjectAnimator.ofFloat(btn, "rotation", 360f, 0f);
                resetAnim.setDuration(300);
                resetAnim.start();
            }
        }

        roundCount = 0;
        isXTurn = true;
        gameActive = true;
        isAITurn = false;
        updateStatusText();
        animateStatusText();
    }

    private void newGame() {
        resetBoard();
        xScore = 0;
        oScore = 0;
        updateScoreDisplay();
        animateScoreReset();
    }

    private void animateScoreReset() {
        ObjectAnimator xScoreAnim = ObjectAnimator.ofFloat(textPlayerX, "scaleX", 1f, 1.3f, 1f);
        ObjectAnimator xScoreAnimY = ObjectAnimator.ofFloat(textPlayerX, "scaleY", 1f, 1.3f, 1f);
        ObjectAnimator oScoreAnim = ObjectAnimator.ofFloat(textPlayerO, "scaleX", 1f, 1.3f, 1f);
        ObjectAnimator oScoreAnimY = ObjectAnimator.ofFloat(textPlayerO, "scaleY", 1f, 1.3f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(xScoreAnim, xScoreAnimY, oScoreAnim, oScoreAnimY);
        animatorSet.setDuration(400);
        animatorSet.start();
    }

    private void updateStatusText() {
        if (gameActive) {
            if (isAIMode) {
                if (isXTurn) {
                    textStatus.setText(getString(R.string.your_turn));
                } else if (!isAITurn) {
                    textStatus.setText(getString(R.string.ai_thinking));
                }
            } else {
                if (isXTurn) {
                    textStatus.setText(getString(R.string.player_x_turn));
                } else {
                    textStatus.setText(getString(R.string.player_o_turn));
                }
            }
        }
    }

    private void updateScoreDisplay() {
        textPlayerX.setText(String.valueOf(xScore));
        textPlayerO.setText(String.valueOf(oScore));
    }

    private void showWinDialog() {
        String winner;
        if (isAIMode) {
            winner = isXTurn ? "You" : "AI";
        } else {
            winner = isXTurn ? "Player X" : "Player O";
        }

        new AlertDialog.Builder(this)
                .setTitle("🎉 Congratulations! 🎉")
                .setMessage(winner + " wins this round!")
                .setPositiveButton("Play Again", (dialog, which) -> resetBoard())
                .setNegativeButton("New Game", (dialog, which) -> newGame())
                .setCancelable(false)
                .show();
    }

    private void showDrawDialog() {
        new AlertDialog.Builder(this)
                .setTitle("🤝 It's a Draw! 🤝")
                .setMessage("Great game! Want to play again?")
                .setPositiveButton("Play Again", (dialog, which) -> resetBoard())
                .setNegativeButton("New Game", (dialog, which) -> newGame())
                .setCancelable(false)
                .show();
    }

    private void playClickSound() {
        if (clickSound != null) {
            try {
                clickSound.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void playWinSound() {
        if (winSound != null) {
            try {
                winSound.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (clickSound != null) {
            clickSound.release();
        }
        if (winSound != null) {
            winSound.release();
        }
        if (aiHandler != null) {
            aiHandler.removeCallbacksAndMessages(null);
        }
    }
}
