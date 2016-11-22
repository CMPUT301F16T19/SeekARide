package com.c301t19.cs.ualberta.seekaride.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.c301t19.cs.ualberta.seekaride.R;
import com.c301t19.cs.ualberta.seekaride.core.Driver;
import com.c301t19.cs.ualberta.seekaride.core.ElasticsearchController;
import com.c301t19.cs.ualberta.seekaride.core.Request;
import com.c301t19.cs.ualberta.seekaride.core.Review;
import com.c301t19.cs.ualberta.seekaride.core.Rider;

public class RCompleteActivity extends Activity {

    private Button confirmP;
    private EditText review;
    private RatingBar ratingBar;

    private String reviewText;
    private int rating;

    private boolean isRider;
    private int requestIndex;
    private Request request;

    //handles getting the review, but doesn't save it anywhere.
    public void write(){
        review = (EditText) findViewById(R.id.complete_Review_Text);

        reviewText = review.getText().toString();
        rating = ratingBar.getNumStars();
    }

    public void move(){
        confirmP = (Button) findViewById(R.id.complete_Confirm_Button);
        ratingBar = (RatingBar) findViewById(R.id.complete_ratingBar);
        if (isRider) {
            confirmP.setText("Pay Driver and Finish");
        }
        else {
            confirmP.setText("Receive Payment and Finish");
        }
        //moves you to the login screen, after saving your review.
        confirmP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write();
                Review review = new Review(reviewText, rating, getIntent().getStringExtra("theirID"));
                Rider.getInstance().leaveReview(review);

                if (isRider) {
                    Rider.getInstance().updateOpenRequests();
                    request = Rider.getInstance().getRequest(request.getId());
                    if (request.didDriverReceivePay()) {
                        // delete request
                    }
                    else {
                        request.riderPay();
                    }
                }
                else {
                    Driver.getInstance().updateAcceptedRequests();
                    request = Driver.getInstance().getRequest(request.getId());
                    if (request.didRiderPay()) {
                        // delete request
                    }
                    else {
                        request.driverReceivePay();
                    }
                }

                finish();
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rcomplete);
        isRider = getIntent().getBooleanExtra("isRider", false);
        requestIndex = getIntent().getIntExtra("requestIndex", -1);
        if (isRider) {
            request = Rider.getInstance().getOpenRequests().get(requestIndex);
        }
        else {
            request = Driver.getInstance().getAcceptedRequests().get(requestIndex);
        }
        move();
    }
}
