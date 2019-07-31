package com.testautomationguru.ocular;

import java.nio.file.Path;

public interface OcularConfiguration {

    OcularConfiguration snapshotPath(Path path);

    OcularConfiguration resultPath(Path path);

    OcularConfiguration globalSimilarity(int cutoff);

    OcularConfiguration saveSnapshot(boolean save);

    Path getSnapshotPath();

    Path getResultPath();

    int getGlobalSimilarity();

    boolean canSaveSnapshot();

    void reset();

}
