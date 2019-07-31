package com.testautomationguru.ocular;

import com.testautomationguru.ocular.snapshot.*;

import java.nio.file.Path;

public class Ocular {

    public static SnapshotBuilder snapshot() {
        return new SnapshotBuilderImpl();
    }

    public static OcularConfiguration config() {
        return OcularConfigImpl.get();
    }

    private static class OcularConfigImpl implements OcularConfiguration {

        private static final OcularConfiguration config = new OcularConfigImpl();

        private Path snapshotpath;
        private Path resultpath;
        private int similarity = 100;
        private boolean savesnapshot = true;

        private OcularConfigImpl() {
        }


        public static OcularConfiguration get() {
            return config;
        }

        public OcularConfiguration snapshotPath(Path path) {
            this.snapshotpath = path;
            return this;
        }

        public OcularConfiguration resultPath(Path path) {
            this.resultpath = path;
            return this;
        }

        public OcularConfiguration globalSimilarity(int cutoff) {
            this.similarity = cutoff;
            return this;
        }

        public Path getSnapshotPath() {
            return snapshotpath;
        }

        public Path getResultPath() {
            return resultpath;
        }

        public int getGlobalSimilarity() {
            return similarity;
        }

        public OcularConfiguration saveSnapshot(boolean save) {
            this.savesnapshot = save;
            return this;
        }

        public boolean canSaveSnapshot() {
            return this.savesnapshot;
        }

        public void reset() {
            this.snapshotpath = null;
            this.resultpath = null;
            this.similarity = 100;
            this.savesnapshot = true;
        }
    }
}
