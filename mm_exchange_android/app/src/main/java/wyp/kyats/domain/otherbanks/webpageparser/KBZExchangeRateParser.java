package wyp.kyats.domain.otherbanks.webpageparser;

import android.os.AsyncTask;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import wyp.kyats.component.util.JSoupUtil;
import wyp.kyats.component.util.Logger;
import wyp.kyats.domain.otherbanks.model.ExchangeRateResponseModel;


/**
 * Created by Wai Yan on 3/13/19.
 */
public class KBZExchangeRateParser extends AsyncTask<String, Void, ExchangeRateResponseModel> {

    private static final String KBZ_BANK_URL = "https://www.kbzbank.com/en/";
    private ParserResponse parserResponse;

    public KBZExchangeRateParser(ParserResponse parserResponse) {
        this.parserResponse = parserResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.parserResponse.onLoading();
    }

    @Override
    protected ExchangeRateResponseModel doInBackground(String... params) {

        String updatedDate = null;
        List<String> rawRates = new ArrayList<>();

        Document pageDocument = JSoupUtil.getData(KBZ_BANK_URL);
        if (pageDocument != null) {

            updatedDate =  pageDocument.select("div[class=row exchange-rate]").select("div[class=col-lg-4 col-sm-4 col-xs-12]").select("strong").text();
            Logger.d(this.getClass(), "KBZ Update Date >> " + updatedDate);

            Elements mElementDataSize = pageDocument.select("div[class=col-lg-2 col-sm-4 col-xs-12]");
            int mElementSize = mElementDataSize.size();

            for (int i = 0; i < mElementSize; i++) {

                Elements rawRateElements = pageDocument.select("div[class=col-lg-2 col-sm-4 col-xs-12]").eq(i);
                String rawRate = rawRateElements.text();
                Logger.d(this.getClass(), "KBZ Rate >> " + rawRate);

                rawRates.add(rawRate);
            }
        }
        return new ExchangeRateResponseModel(updatedDate, rawRates);
    }

    @Override
    protected void onPostExecute(ExchangeRateResponseModel exchangeRateResponseModel) {
        super.onPostExecute(exchangeRateResponseModel);
        this.parserResponse.onSuccess(exchangeRateResponseModel);
    }

    public interface ParserResponse {

        void onLoading();

        void onSuccess(ExchangeRateResponseModel exchangeRateResponseModel);
    }
}
