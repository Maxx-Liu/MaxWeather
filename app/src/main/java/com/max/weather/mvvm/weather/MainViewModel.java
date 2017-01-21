package com.max.weather.mvvm.weather;

import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.max.weather.databinding.ActivityMainBinding;
import com.max.weather.mvvm.weather.bean.CityBean;
import com.max.weather.mvvm.weather.bean.NowBean;
import com.max.weather.mvvm.weather.bean.WeatherBean;
import com.max.weather.utils.WeatherUtil;

import rx.Observer;

/**
 * @auther MaxLiu
 * @time 2017/1/19
 */

public class MainViewModel {

    private static final String TAG = "MainViewModel";
    //方圆卡通字体
    private static final String CUSTOM_FONT = "fonts/fangyuan.ttf";

    private AppCompatActivity activity;
    private ActivityMainBinding mBinding;
    private MainConstract.IModel IModel;

    MainViewModel(AppCompatActivity activity, ActivityMainBinding binding) {
        mBinding = binding;
        this.activity = activity;
        IModel = new MainModel();
        initTextStyle();
    }

    /**
     * 根据获取的经纬度请求网络数据并刷新View
     *
     * @param longitude 经度
     * @param latitude  纬度
     */
    void loadWeather(String longitude, String latitude) {
        IModel.getWeatherFromLocation(MainModel.LOCATION_BAIDU, longitude, latitude)
                .subscribe(new Observer<WeatherBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "failed -> " + e.toString());
                    }

                    @Override
                    public void onNext(WeatherBean weatherBean) {
                        Log.d(TAG, "success -> " + weatherBean.toString());
                        NowBean nowWeather = weatherBean.getShowapiResBody().getNow();
                        CityBean cityInfo = weatherBean.getShowapiResBody().getCityInfo();
                        mBinding.setNowWeather(nowWeather);
                        mBinding.setWeatherIcon(nowWeather.getWeatherCode());
                        mBinding.setNowCity(cityInfo);
                    }
                });
    }

    /**
     * 初始化字体
     */
    private void initTextStyle() {
        Typeface customFont = Typeface.createFromAsset(activity.getAssets(), CUSTOM_FONT);
        mBinding.infoWeather.setTypeface(customFont);
        mBinding.infoCity.setTypeface(customFont);
    }

    /**
     * 根据天气信息更新天气图标
     *
     * @param view        ImageView
     * @param weatherCode 数据得到的weatherCode
     */
    @BindingAdapter({"icon"})
    public static void setWeatherIcon(ImageView view, String weatherCode) {
        if (weatherCode != null) {
            view.setImageResource(WeatherUtil.getRsId(weatherCode));
        }
    }
}
