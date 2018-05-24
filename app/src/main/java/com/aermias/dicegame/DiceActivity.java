package com.aermias.dicegame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class DiceActivity extends AppCompatActivity {

    static Integer numberActuallyRolled = -1;

    static Integer guessesLeft;
    static Integer[] scores;

    static Integer gamesPlayed = 0;

    static Integer[] scoreTemplate = new Integer[]{ 100, 500, 1000 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice);

        final Button rollDie = findViewById(R.id.rollDie);
        final TextView dieRolled = findViewById(R.id.dieRolled);
        final TextView chooseNumber = findViewById(R.id.chooseNumber);
        final EditText numberGuessed = findViewById(R.id.numberGuessed);
        final Button submitGuess = findViewById(R.id.submitGuess);
        final TextView numberRolled = findViewById(R.id.numberRolled);
        final Button playAgain = findViewById(R.id.playAgain);

        rollDie.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // sets # of guesses left to three
                guessesLeft = 3;

                // rolls die
                numberActuallyRolled = roll_Die();

                // displays all views and buttons
                dieRolled.setVisibility(View.VISIBLE);
                chooseNumber.setVisibility(View.VISIBLE);
                numberGuessed.setVisibility(View.VISIBLE);
                submitGuess.setVisibility(View.VISIBLE);

                // disables rolling die again
                rollDie.setEnabled(false);
            }
        });

        numberGuessed.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // automatically sets submit to false if text is empty
                if (s.toString().length() < 1) {
                    submitGuess.setEnabled(false);
                    return;
                }

                Integer number_entered = Integer.valueOf(s.toString());
                Boolean withinBounds = number_entered > 0 && number_entered < 7;

                // enable submit button if number entered is 1-6
                submitGuess.setEnabled(withinBounds);
            }
        });

        submitGuess.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (guessesLeft == 0) return;

                // immediately removes one guess
                guessesLeft--;

                Integer numberActuallyGuessed = Integer.valueOf(numberGuessed.getText().toString());
                Boolean too_low = numberActuallyGuessed < numberActuallyRolled;

                Boolean correct = numberActuallyGuessed.equals(numberActuallyRolled);

                String response = getResources().getString(R.string.incorrect) + " " + (too_low ? "low" : "high") + ".";
                response += "\n\n" + getResources().getString(R.string.tries_left).replace("-n", guessesLeft.toString());

                if (guessesLeft == 1) {
                    response = response.replace("tries", "try");
                } else if (guessesLeft < 1 && !correct) {
                    numberRolled.setText(getResources().getString(R.string.no_tries) + " " + numberActuallyRolled + ".");
                    endGame();
                    return;
                }

                if (correct) {
                    response = getResources().getString(R.string.correct).replace("-n", scoreTemplate[guessesLeft].toString());
                    endGame();
                }

                numberRolled.setText(response);
            }

            public void endGame() {
                // disables EditText and Button, allowing the user to play again
                numberGuessed.setEnabled(false);
                submitGuess.setEnabled(false);

                playAgain.setVisibility(View.VISIBLE);
            }
        });

        playAgain.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                //* resets activity (does not preserve scores)
                    finish();
                    startActivity(getIntent());


                /*
                // manually sets all values to enabled
                rollDie.setEnabled(true);
                numberGuessed.setEnabled(true);
                playAgain.setEnabled(true);

                // only one disabled (default setting)
                submitGuess.setEnabled(false);


                // manually sets views to invisible
                dieRolled.setVisibility(View.INVISIBLE);
                chooseNumber.setVisibility(View.INVISIBLE);
                numberGuessed.setVisibility(View.INVISIBLE);
                submitGuess.setVisibility(View.INVISIBLE);
                playAgain.setVisibility(View.INVISIBLE);

                // manually removes text
                numberGuessed.setText("");
                numberRolled.setText("");
                */
            }
        });
    }

    public Integer roll_Die() {
        return new Random().nextInt(6) + 1;
    }
}
