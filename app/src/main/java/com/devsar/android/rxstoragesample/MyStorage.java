package com.devsar.android.rxstoragesample;

import android.content.Context;

import com.devsar.android.rxstorage.Storage;
import com.devsar.android.rxstorage.StoredField;

import java.util.Set;

/**
 * Created by Juan on 4/4/17.
 */

@Storage(name = "my_super_secret_storage", mode = Context.MODE_APPEND)
public interface MyStorage {
    @StoredField(key = "secret_access_token") String accessToken();
    @StoredField(key = "superCoolLongValue", withDefault = "45") Long longValue();
    @StoredField(withDefault = "true") Boolean boolValue();
    @StoredField Float floatValue();
    @StoredField Integer intValue();
    @StoredField Set<String> multipleStrings();
}
