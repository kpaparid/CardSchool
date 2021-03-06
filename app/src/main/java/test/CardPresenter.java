package test;

import android.content.Context;
import android.os.Handler;
import android.speech.tts.TextToSpeech;

import com.example.marmi.cardschool.data.WordController;

import java.util.Locale;

public class CardPresenter {

    protected CardView cV;
    public WordController wordController;
    public String target = "en";
    private String mode;
    public String from;
    public String to;
    public Context context;
    public TextToSpeech t1;
    public TextToSpeech t2;






    public CardPresenter(Context context, CardView cV, WordController wc) {

        this.context = context;
        this.cV = cV;
        if (cV.getArguments() != null) {
            from = cV.getArguments().getString("nfrom");
            to = cV.getArguments().getString("nto");
            mode =cV.getArguments().getString("mode");
            wordController = (WordController) cV.getArguments().getSerializable("wc");
        }
        System.out.println(wordController.getFullWordText());
        t2=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        t1=new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.GERMAN);
                }
            }
        });
    }



    public void backClick() {

            wordController.moveToPrevious();
            cV.origUI(wordController);
            cV.Orig = true;
    }


    public String getTarget(){
        return target;
    }
    public void setTarget(String target) {
        this.target = target;
    }
    public void playClick() {
        if(!cV.Orig){
            System.out.println("orig");
            wordController.moveToNext();
            cV.origUI(wordController);
            t1.speak(wordController.getArticle()+" "+wordController.getWordText(), TextToSpeech.QUEUE_FLUSH, null);
            cV.Orig = true;

        }else {
            System.out.println("trans");
            cV.transUI(wordController,target);
            t2.speak(wordController.getEn_translated(), TextToSpeech.QUEUE_FLUSH, null);
            cV.Orig = false;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                textClick();
            }
        }, 5000);   //5 seconds

    }

    public void textClick() {
        if(!cV.Orig){
            System.out.println("orig");
            wordController.moveToNext();
            cV.origUI(wordController);
            cV.Orig = true;

        }else {
            System.out.println("trans");
            cV.transUI(wordController,target);
            cV.Orig = false;
        }
    }

    public WordController getWordController() {
        return wordController;
    }
}
