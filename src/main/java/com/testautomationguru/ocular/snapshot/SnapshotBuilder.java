package com.testautomationguru.ocular.snapshot;

import com.testautomationguru.ocular.sample.SampleBuilder;

import java.nio.file.Path;

public interface SnapshotBuilder {

    <T> SnapshotBuilder from(Class<T> pageClass);

    SnapshotBuilder from(Object object);

    SnapshotBuilder from(Path path);

    SnapshotBuilder replaceAttribute(String id, String value);

    SampleBuilder sample();

}
