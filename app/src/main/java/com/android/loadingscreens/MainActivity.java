package com.android.loadingscreens;


import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.PathInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.loadingscreens.utils.Constants;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {


    protected Context mContext;
    private FrameLayout mainLayout;
    private ImageButton downloadButton;
    private ImageButton nextButton;
    private ImageButton backgroundButton;
    private TextView downloadTextView;
    private TextView progressTextView;
    private TextView percentTextView;
    private TextView finishedTextView;
    private ImageView ripplePinkWhite;
    private ImageView checkView;
    private FrameLayout progressLayout;
    private FrameLayout revealLayout;
    /**
     * Barra de progreso
     */
    private ProgressBar mProgress;
    private int mProgressStatus = Constants.PROGRESSBAR_MAX;
    private CountDownTimer progressBarTimer;

    private AnimationDrawable rippleAnimation;
    private ScaleAnimation showCheck;
    private ScaleAnimation hidePercent;

    private ScaleAnimation showNext;
    private ScaleAnimation hideDownload;

    private ScaleAnimation showProgressLayout;

    private TranslateAnimation progressTextTransitionIN;
    private TranslateAnimation downloadTextTransitionOUT;
    private TranslateAnimation progressTextTransitionOUT;
    private TranslateAnimation finishedTextTransitionIN;


    private ArrayList<Integer> primaryColors;
    private ArrayList<Integer> primaryColorsDark;
    private ArrayList<Integer> accentColors;
    private ArrayList<Drawable> progressBars;
    private ArrayList<Drawable> roundButtons;
    private int indexColor = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.v(Constants.Log.METHOD, "MainActivity - OnCreate");

		getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);

        mContext = this;

        setContentView(R.layout.main_layout);
        mainLayout = (FrameLayout) findViewById(R.id.main_layout);
        downloadButton = (ImageButton) findViewById(R.id.download_button);
        nextButton = (ImageButton) findViewById(R.id.next_button);
        backgroundButton = (ImageButton) findViewById(R.id.background_button);
        downloadTextView = (TextView)  findViewById(R.id.download_textview);
        progressTextView = (TextView)  findViewById(R.id.progress_textview);
        percentTextView = (TextView)  findViewById(R.id.percent_tv);
        finishedTextView = (TextView)  findViewById(R.id.finished_textview);
        checkView = (ImageView)  findViewById(R.id.check_view);
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
        progressLayout = (FrameLayout) findViewById(R.id.progressLayout);
        revealLayout = (FrameLayout) findViewById(R.id.reveal_layout);

        mProgress.setMax(Constants.PROGRESSBAR_MAX);
        //Iniciamos la barra de progreso a 0 para la posterior animación que la rellena
        mProgress.setProgress(0);

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.v(Constants.Log.METHOD, "MainActivity - downloadButton.onClick");

                progressLayout.startAnimation(showProgressLayout);
                progressTextView.startAnimation(progressTextTransitionIN);
                downloadTextView.startAnimation(downloadTextTransitionOUT);

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.v(Constants.Log.METHOD, "MainActivity - nextButton.onClick");

                startReveal();

            }
        });

        prepareAnimations();
        prepareColors();
    }

    private void prepareColors() {

        primaryColors = new ArrayList<Integer>();
        primaryColorsDark = new ArrayList<Integer>();
        accentColors = new ArrayList<Integer>();
        progressBars = new ArrayList<Drawable>();
        roundButtons = new ArrayList<Drawable>();

        primaryColors.add(getColorV(R.color.colorIndigo));
        primaryColors.add(getColorV(R.color.colorGreen));
        primaryColors.add(getColorV(R.color.colorTeal));
        primaryColorsDark.add(getColorV(R.color.colorIndigoDark));
        primaryColorsDark.add(getColorV(R.color.colorGreenDark));
        primaryColorsDark.add(getColorV(R.color.colorTealDark));
        accentColors.add(getColorV(R.color.colorPink));
        accentColors.add(getColorV(R.color.colorDeepOrange));
        accentColors.add(getColorV(R.color.colorAmber));
        progressBars.add(getDrawableV(R.drawable.progress_bar));
        progressBars.add(getDrawableV(R.drawable.progress_bar_deep_orange));
        progressBars.add(getDrawableV(R.drawable.progress_bar_amber));
        roundButtons.add(getDrawableV(R.drawable.round_shape));
        roundButtons.add(getDrawableV(R.drawable.round_shape_deep_orange));
        roundButtons.add(getDrawableV(R.drawable.round_shape_amber));
    }

    private void prepareAnimations() {

        showCheck = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        showCheck.setDuration(200);
        showCheck.setFillAfter(true);
        showCheck.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                Log.v(Constants.Log.TIMER, "MainActivity - showCheck.onAnimationStart");

                checkView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.TIMER, "MainActivity - showCheck.onAnimationStart");

                downloadButton.startAnimation(hideDownload);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        hidePercent = new ScaleAnimation(1f, 0f, 1f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        hidePercent.setDuration(200);
        hidePercent.setFillAfter(true);
        hidePercent.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                Log.v(Constants.Log.TIMER, "MainActivity - hidePercent.onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.TIMER, "MainActivity - hidePercent.onAnimationStart");

                percentTextView.setVisibility(View.INVISIBLE);
                checkView.startAnimation(showCheck);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        showNext = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        showNext.setDuration(200);
        showNext.setFillAfter(true);
        showNext.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                Log.v(Constants.Log.TIMER, "MainActivity - showNext.onAnimationStart");

                nextButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        hideDownload = new ScaleAnimation(1f, 0f, 1f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        hideDownload.setDuration(200);
        hideDownload.setFillAfter(true);
        hideDownload.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                Log.v(Constants.Log.TIMER, "MainActivity - hideDownload.onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.TIMER, "MainActivity - hideDownload.onAnimationStart");

                downloadButton.setVisibility(View.INVISIBLE);
                nextButton.startAnimation(showNext);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        progressTextTransitionIN = new TranslateAnimation(
                -600, 0, 0, 0);
        progressTextTransitionIN.setDuration(400);
        progressTextTransitionIN.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            progressTextTransitionIN.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        }
        progressTextTransitionIN.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.TIMER, "MainActivity - progressTextTransitionIN.onAnimationEnd");

                progressTextView.setVisibility(View.VISIBLE);

                startProgressBarAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        progressTextTransitionOUT = new TranslateAnimation(
                0, 600, 0, 0);
        progressTextTransitionOUT.setDuration(400);
        progressTextTransitionOUT.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            progressTextTransitionOUT.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        }
        progressTextTransitionOUT.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.TIMER, "MainActivity - progressTextTransitionOUT.onAnimationEnd");

                progressTextView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        downloadTextTransitionOUT = new TranslateAnimation(
                0, 600, 0, 0);
        downloadTextTransitionOUT.setDuration(400);
        downloadTextTransitionOUT.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            downloadTextTransitionOUT.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        }
        downloadTextTransitionOUT.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.TIMER, "MainActivity - downloadTextTransitionOUT.onAnimationEnd");

                downloadTextView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        finishedTextTransitionIN = new TranslateAnimation(
                -600, 0, 0, 0);
        finishedTextTransitionIN.setDuration(400);
        finishedTextTransitionIN.setFillAfter(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            finishedTextTransitionIN.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                    getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        }
        finishedTextTransitionIN.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                Log.v(Constants.Log.TIMER, "MainActivity - finishedTextTransitionIN.onAnimationStart");

                finishedTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Log.v(Constants.Log.TIMER, "MainActivity - finishedTextTransitionIN.onAnimationEnd");

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        showProgressLayout = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        showProgressLayout.setDuration(400);
        showProgressLayout.setFillAfter(true);
        showProgressLayout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                Log.v(Constants.Log.TIMER, "MainActivity - showProgressLayout.onAnimationStart");

                progressLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void startProgressBarAnimation(){

        Log.v(Constants.Log.TIMER, "MainActivity - startProgressBarAnimation");

        //Creamos el hilo.
        //Hay que asegurase de que se crea en el hilo principal ya que accede a la barra de progreso.
        progressBarTimer = new CountDownTimer(Constants.PROGRESSBAR_INITIAL_TIME_ANIMATION, Constants.PROGRESSBAR_REFRESH_INTERVAL) {

            /* 
             * Actualiza la barra de progreso.
             * (non-Javadoc)
             * @see android.os.CountDownTimer#onTick(long)
             */
            @Override
            public void onTick(long millisUntilFinished) {

                mProgressStatus = Constants.PROGRESSBAR_MAX - (int) ((double) millisUntilFinished / (double ) Constants.PROGRESSBAR_INITIAL_TIME_ANIMATION
                        * Constants.PROGRESSBAR_MAX);
                Log.v(Constants.Log.TIMER, "CountDownTimer.onTick MainActivity - ProgressBar MillisUntilFinished: "+millisUntilFinished);
                Log.v(Constants.Log.TIMER, "CountDownTimer.onTick MainActivity - ProgressBarStatus: "+mProgressStatus);
                //Update the progress bar
                mProgress.setProgress(mProgressStatus);

                int percent = (mProgressStatus)/10 + 1;
                percentTextView.setText(""+percent+" %");
            }

            @Override
            public void onFinish() {

                Log.v(Constants.Log.TIMER, "MainActivity - startProgressBarInitialAnimation.onFinish - ProgressBar finished");

                mProgressStatus = Constants.PROGRESSBAR_MAX;
                mProgress.setProgress(mProgressStatus);

                percentTextView.startAnimation(hidePercent);
                progressTextView.startAnimation(progressTextTransitionOUT);
                finishedTextView.startAnimation(finishedTextTransitionIN);
            }
        };
        progressBarTimer.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startReveal() {

        Log.v(Constants.Log.METHOD, "MainActivity - startReveal");

        //Calculamos el centro de la animación
        int cx = (downloadButton.getLeft() + downloadButton.getRight()) / 2;
        int cy = (downloadButton.getTop() + downloadButton.getBottom()) / 2;

        //Calculamos el radio de la animación
        int finalRadius = (int) Math.sqrt(Math.pow(revealLayout.getWidth(), 2) +
                Math.pow(revealLayout.getHeight(), 2));

        //Creamos la animación
        Animator anim =
                ViewAnimationUtils.createCircularReveal(revealLayout,
                        cx, cy, 0, finalRadius);
        anim.setDuration(600);

        //Hacemos visible la vista y empezamos la animación
        anim.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                Log.v(Constants.Log.METHOD, "MainActivity - startReveal.onAnimationStart");

                //Hacemos visible el reveal y le cambiamos el color
                revealLayout.setVisibility(View.VISIBLE);
                revealLayout.setBackgroundColor(accentColors.get(indexColor));

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.v(Constants.Log.METHOD, "MainActivity - startReveal.onAnimationEnd");

                resetState();
                changeTheme();
                startUnveal();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        anim.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                getApplicationContext(), android.R.interpolator.fast_out_linear_in)));
        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startUnveal() {

        Log.v(Constants.Log.METHOD, "MainActivity - startUnveal");

        //Calculamos el centro de la animación
        int cx = (downloadButton.getLeft() + downloadButton.getRight()) / 2;
        int cy = (downloadButton.getTop() + downloadButton.getBottom()) / 2;

        //Calculamos el radio de la animación
        int initialRadius = (int) Math.sqrt(Math.pow(revealLayout.getWidth(), 2) +
                Math.pow(revealLayout.getHeight(), 2));

        //Creamos la animación
        Animator anim =
                ViewAnimationUtils.createCircularReveal(revealLayout,
                        cx, cy, initialRadius, 0);
        anim.setDuration(600);

        //Hacemos visible la vista y empezamos la animación
        anim.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                Log.v(Constants.Log.METHOD, "MainActivity - startUnveal.onAnimationStart");

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.v(Constants.Log.METHOD, "MainActivity - startUnveal.onAnimationEnd");

                revealLayout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        //anim.setStartDelay(getResources().getInteger(R.integer.delay_reveal_like));
        anim.setInterpolator((PathInterpolator) (AnimationUtils.loadInterpolator(
                getApplicationContext(), android.R.interpolator.fast_out_slow_in)));
        anim.start();
    }

    private void resetState() {
        nextButton.clearAnimation();
        nextButton.setVisibility(View.INVISIBLE);
        downloadButton.clearAnimation();
        downloadButton.setVisibility(View.VISIBLE);
        checkView.clearAnimation();
        checkView.setVisibility(View.INVISIBLE);
        finishedTextView.clearAnimation();
        finishedTextView.setVisibility(View.INVISIBLE);
        progressLayout.clearAnimation();
        progressLayout.setVisibility(View.INVISIBLE);
        progressTextView.clearAnimation();
        progressTextView.setVisibility(View.INVISIBLE);
        percentTextView.clearAnimation();
        percentTextView.setVisibility(View.VISIBLE);
        downloadTextView.clearAnimation();
        downloadTextView.setVisibility(View.VISIBLE);
        mProgress.setProgress(0);
        percentTextView.setText("0 %");
    }

    private void changeTheme() {

        Log.v(Constants.Log.METHOD, "MainActivity - changeTheme");

        indexColor++;

        if (indexColor == primaryColors.size()) indexColor = 0;

        mainLayout.setBackgroundColor(primaryColors.get(indexColor));
        mProgress.setProgressDrawable((progressBars.get(indexColor)));
        backgroundButton.setBackground(roundButtons.get(indexColor));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(primaryColorsDark.get(indexColor));
        }
    }

    private Drawable getDrawableV(int resID) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

          return getResources().getDrawable(resID, null);
        }
        else {
            return getResources().getDrawable(resID);
        }
    }

    private int getColorV(int resID){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            return getResources().getColor(resID, null);
        } else {

           return getResources().getColor(resID);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }

    @Override
	protected void onPause() {

    	Log.v(Constants.Log.METHOD, "MainActivity - OnPause");

		super.onPause();
	}

    @Override
    protected void onRestart() {
        Log.v(Constants.Log.METHOD, "MainActivity - onRestart");

        super.onRestart();
    }

    @Override
    protected void onStop() {

        Log.v(Constants.Log.METHOD, "MainActivity - onStop");

        super.onStop();
    }

	@Override
	protected void onDestroy() {
		
		Log.v(Constants.Log.METHOD, "MainActivity - OnDestroy");
		
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		Log.v(Constants.Log.METHOD, "MainActivity - OnResume");

		super.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.v(Constants.Log.METHOD, "MainActivity - onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}
	
    @Override
    public void onBackPressed() {
    	
    	Log.v(Constants.Log.METHOD, "MainActivity - onBackPressed");
    }

}
