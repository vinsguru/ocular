package com.testautomationguru.ocular.snapshot;

import com.testautomationguru.ocular.Ocular;
import com.testautomationguru.ocular.exception.OcularException;
import com.testautomationguru.ocular.sample.*;

import java.nio.file.*;

public class SnapshotBuilderImpl implements SnapshotBuilder {

    private SnapshotAttributes snapshotAttribute = new SnapshotAttributes();

    public <T> SnapshotBuilder from(Class<T> pageClass) {
        getSnapAnnotation(pageClass);
        return this;
    }

    public SnapshotBuilder from(Object object) {
        getSnapAnnotation(object.getClass());
        return this;
    }

    public SnapshotBuilder from(Path path) {
        resolvePath(path);
        return this;
    }

    public SnapshotBuilder replaceAttribute(String param, String value) {
        String newPath = snapshotAttribute.getSnapshotPath().toString().replaceAll("#\\{" + param + "}", value);
        snapshotAttribute.setSnapshotPath(Paths.get(newPath));
        return this;
    }

    public SampleBuilder sample() {
        int similarity = this.snapshotAttribute.getSimilarity() > -1 ? this.snapshotAttribute.getSimilarity() : Ocular.config().getGlobalSimilarity();

        if (similarity < 0 || similarity > 100) {
            throw new IllegalArgumentException("Similarity should be between 0 and 100. But the actual is " + similarity);
        }

        snapshotAttribute.setSimilarity(similarity);
        return new SampleBuilderImpl(this.snapshotAttribute);
    }

    private void getSnapAnnotation(Class<?> klass) {

        if (!klass.isAnnotationPresent(Snap.class))
            throw new OcularException("Snap annotation is not present for type : " + klass.getName());

        Snap snap = klass.getAnnotation(Snap.class);
        snapshotAttribute.setSimilarity(snap.similarity());
        resolvePath(Paths.get(snap.value()));
    }

    private void resolvePath(Path path) {
        path = path.isAbsolute() ? path : Ocular.config().getSnapshotPath().resolve(path);
        this.snapshotAttribute.setSnapshotPath(path);
    }
}
