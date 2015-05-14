package com.amine.torf;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.andtinder.model.CardModel;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;

public class ShowCase extends Activity {
    private CardContainer mCardContainerIntro;
    private CardModel cardModelIntro;
    private SimpleCardStackAdapter adapterIntro;
    private String categoryname;
    private LinearLayout linearBoundQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_case);

        linearBoundQ = (LinearLayout) findViewById(R.id.layoutBoundQ);
        linearBoundQ.setBackgroundColor(Color.LTGRAY);

        categoryname = getIntent().getStringExtra("categoryname");
        adapterIntro = new SimpleCardStackAdapter(this);

        cardModelIntro = new CardModel("");

        mCardContainerIntro = (CardContainer) findViewById(R.id.layoutCardQuestion);
        cardModelIntro
                .setTitle(" Swipe right if the answer is true \n left if it is false  ");

        cardModelIntro
                .setOnCardDimissedListener(new CardModel.OnCardDimissedListener() {
                    public void onRight() {
                        startActivity();
                    }

                    public void onLeft() {
                        startActivity();

                    }
                });

        adapterIntro.add(cardModelIntro);
        mCardContainerIntro.setAdapter(adapterIntro);
    }

    public void startActivity() {
        Intent i = new Intent(ShowCase.this, Timer_questions.class);
        i.putExtra("categoryname", categoryname);
        finish();
        startActivity(i);
    }
}
