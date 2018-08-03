package io.androidovshchik.antiyoy.menu.slider;

public class SbDefault extends SliderBehavior {
    public String getValueString(SliderYio sliderYio) {
        return "" + sliderYio.getCurrentRunnerIndex();
    }
}
