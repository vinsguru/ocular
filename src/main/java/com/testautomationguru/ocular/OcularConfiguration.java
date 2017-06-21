package com.testautomationguru.ocular;

import java.nio.file.Path;

public interface OcularConfiguration {
    
    public OcularConfiguration snapshotPath(Path path);
    public OcularConfiguration resultPath(Path path);
    public OcularConfiguration globalSimilarity(int cutoff);
    public OcularConfiguration saveSnapshot(boolean save);
    public Path getSnapshotPath();
    public Path getResultPath();
    public int getGlobalSimilarity();
    public boolean canSaveSnapshot();
    public void reset();

}
