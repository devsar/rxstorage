package com.devsar.android.rxstorage;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Juan on 30/3/17.
 */

public class ReactiveStorage {

    private final SharedPreferences preferences;
    private final Scheduler scheduler;

    public ReactiveStorage(SharedPreferences preferences) {
        this.preferences = preferences;
        this.scheduler = Schedulers.io();
    }

    public ReactiveStorage(SharedPreferences preferences, Scheduler scheduler) {
        this.preferences = preferences;
        this.scheduler = scheduler;
    }

    public ReactiveStorage(Context context, String name, int mode) {
        this(context, name, mode, Schedulers.io());
    }

    public ReactiveStorage(Context context, String name, int mode, Scheduler scheduler) {
        this.preferences = context.getSharedPreferences(name, mode);
        this.scheduler = scheduler;
    }

    private void withEditorDo(Consumer<SharedPreferences.Editor> action) throws Exception {
        SharedPreferences.Editor editor = preferences.edit();
        action.accept(editor);
        editor.apply();
    }

    private <A> Single<A> getWith(final BiFunction<String, A, A> get,
                                  final String key, final A defaultValue) {
        return Single.fromCallable(() -> get.apply(key, defaultValue)).subscribeOn(scheduler);
    }

    protected final Completable saveWith(final Consumer<SharedPreferences.Editor> action) {
        return Completable.fromAction(() -> withEditorDo(action)).subscribeOn(scheduler);
    }

    protected final Completable saveString(final String key, final String value) {
        return saveWith(e -> e.putString(key, value));
    }

    protected final Single<String> getString(final String key, final String defaultValue) {
        return getWith(preferences::getString, key, defaultValue);
    }

    protected final Completable saveBoolean(final String key, final boolean value) {
        return saveWith(e -> e.putBoolean(key, value));
    }

    protected final Single<Boolean> getBoolean(final String key, final boolean defaultValue) {
        return getWith(preferences::getBoolean, key, defaultValue);
    }

    protected final Completable saveInt(final String key, final int value) {
        return saveWith(e -> e.putInt(key, value));
    }

    protected final Single<Integer> getInt(final String key, final int defaultValue) {
        return getWith(preferences::getInt, key, defaultValue);
    }

    protected final Completable saveFloat(final String key, final float value) {
        return saveWith(e -> e.putFloat(key, value));
    }

    protected final Single<Float> getFloat(final String key, final float defaultValue) {
        return getWith(preferences::getFloat, key, defaultValue);
    }

    protected final Completable saveLong(final String key, final long value) {
        return saveWith(e -> e.putLong(key, value));
    }

    protected final Single<Long> getLong(final String key, final long defaultValue) {
        return getWith(preferences::getLong, key, defaultValue);
    }

    protected final Completable saveStringSet(final String key, final Set<String> value) {
        return saveWith(e -> e.putStringSet(key, value));
    }

    protected final Single<Set<String>> getStringSet(final String key, final Set<String> defaultValue) {
        return getWith(preferences::getStringSet, key, defaultValue);
    }
}
