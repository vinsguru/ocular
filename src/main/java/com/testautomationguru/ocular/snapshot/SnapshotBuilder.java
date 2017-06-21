package com.testautomationguru.ocular.snapshot;

import java.nio.file.Path;

import com.testautomationguru.ocular.sample.SampleBuilder;

public interface SnapshotBuilder {
    
    public <T> SnapshotBuilder from(Class<T> pageClass);
    public SnapshotBuilder from(Object object);
    public SnapshotBuilder from(Path path);
    public SnapshotBuilder replaceAttribute(String id, String value);
    public SampleBuilder sample();

}
