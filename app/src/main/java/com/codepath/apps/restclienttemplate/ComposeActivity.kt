package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var btnTweet: Button
    lateinit var countDown: TextView

    lateinit var client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)
        countDown = findViewById(R.id.countDown)
        client = TwitterApplication.getRestClient(this)
        //Handling the cound og the user

        etCompose.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Get length
                val character = etCompose.length()
                // Calculate negative
                val displayedChar = 280 - character

                //Convert from int to string
                displayedChar.toString()

                countDown.setText(displayedChar)

            }

        })

        //Handling the user's click on tweet button
        btnTweet.setOnClickListener {
            //Grab the content of edittext (etCompose)
            countDown.setText("")

            val tweetContent = etCompose.text.toString()

            //1. Make sure the tweet isn't empty
            if (tweetContent.isEmpty()){

                Toast.makeText(this, "Empty tweets not allowed", Toast.LENGTH_SHORT).show()
                //Learn snackBar message
            } else
                //2. Make sure the tweet is under character count
                if (tweetContent.length > 280){
                Toast.makeText(this, "Tweet is too long! Limit is 280 characters", Toast.LENGTH_SHORT).show()
                } else {

                    client.publishTweet(tweetContent, object:JsonHttpResponseHandler(){



                        override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {

                            Log.i(TAG, "Successfully published Tweet!")


                            val tweet = Tweet.fromJson(json.jsonObject)

                            val intent = Intent()
                            intent.putExtra("tweet", tweet)

                            setResult(RESULT_OK, intent)
                            finish()

                        }

                        override fun onFailure(
                            statusCode: Int,
                            headers: Headers?,
                            response: String?,
                            throwable: Throwable?
                        ) {
                            Log.e(TAG, "Failed to publish tweet", throwable)
                        }




                    })
                    //TODO: Make an api call to twitter to publish tweet
                }
        }
    }
    companion object{
        val TAG = "ComposeActivity"
    }
}