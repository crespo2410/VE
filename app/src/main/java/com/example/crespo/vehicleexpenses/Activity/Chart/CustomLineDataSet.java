package com.example.crespo.vehicleexpenses.Activity.Chart;

import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.data.BaseDataSet;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.List;



public class CustomLineDataSet extends BaseDataSet<Entry> implements ILineDataSet {
    @Override
    public LineDataSet.Mode getMode() {
        return null;
    }

    @Override
    public float getCubicIntensity() {
        return 0;
    }

    @Override
    public boolean isDrawCubicEnabled() {
        return false;
    }

    @Override
    public boolean isDrawSteppedEnabled() {
        return false;
    }

    @Override
    public float getCircleRadius() {
        return 0;
    }

    @Override
    public float getCircleHoleRadius() {
        return 0;
    }

    @Override
    public int getCircleColor(int index) {
        return 0;
    }

    @Override
    public int getCircleColorCount() {
        return 0;
    }

    @Override
    public boolean isDrawCirclesEnabled() {
        return false;
    }

    @Override
    public int getCircleHoleColor() {
        return 0;
    }

    @Override
    public boolean isDrawCircleHoleEnabled() {
        return false;
    }

    @Override
    public DashPathEffect getDashPathEffect() {
        return null;
    }

    @Override
    public boolean isDashedLineEnabled() {
        return false;
    }

    @Override
    public IFillFormatter getFillFormatter() {
        return null;
    }

    @Override
    public int getFillColor() {
        return 0;
    }

    @Override
    public Drawable getFillDrawable() {
        return null;
    }

    @Override
    public int getFillAlpha() {
        return 0;
    }

    @Override
    public float getLineWidth() {
        return 0;
    }

    @Override
    public boolean isDrawFilledEnabled() {
        return false;
    }

    @Override
    public void setDrawFilled(boolean enabled) {

    }

    @Override
    public boolean isVerticalHighlightIndicatorEnabled() {
        return false;
    }

    @Override
    public boolean isHorizontalHighlightIndicatorEnabled() {
        return false;
    }

    @Override
    public float getHighlightLineWidth() {
        return 0;
    }

    @Override
    public DashPathEffect getDashPathEffectHighlight() {
        return null;
    }

    @Override
    public int getHighLightColor() {
        return 0;
    }

    @Override
    public float getYMin() {
        return 0;
    }

    @Override
    public float getYMax() {
        return 0;
    }

    @Override
    public float getXMin() {
        return 0;
    }

    @Override
    public float getXMax() {
        return 0;
    }

    @Override
    public int getEntryCount() {
        return 0;
    }

    @Override
    public void calcMinMax() {

    }

    @Override
    public void calcMinMaxY(float fromX, float toX) {

    }

    @Override
    public Entry getEntryForXValue(float xValue, float closestToY, DataSet.Rounding rounding) {
        return null;
    }

    @Override
    public Entry getEntryForXValue(float xValue, float closestToY) {
        return null;
    }

    @Override
    public List<Entry> getEntriesForXValue(float xValue) {
        return null;
    }

    @Override
    public Entry getEntryForIndex(int index) {
        return null;
    }

    @Override
    public int getEntryIndex(float xValue, float closestToY, DataSet.Rounding rounding) {
        return 0;
    }

    @Override
    public int getEntryIndex(Entry e) {
        return 0;
    }

    @Override
    public boolean addEntry(Entry e) {
        return false;
    }

    @Override
    public void addEntryOrdered(Entry e) {

    }

    @Override
    public boolean removeEntry(Entry e) {
        return false;
    }

    @Override
    public void clear() {

    }
}
