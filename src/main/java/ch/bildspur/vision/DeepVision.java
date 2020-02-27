package ch.bildspur.vision;

import ch.bildspur.vision.deps.Dependency;
import ch.bildspur.vision.deps.Repository;
import processing.core.PApplet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DeepVision {

    public DeepVision(PApplet sketch) {
        Repository.localStorageDirectory = Paths.get(sketch.dataPath("networks"));
    }

    private void prepareDependencies(Dependency... dependencies) {
        try {
            Files.createDirectories(Repository.localStorageDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Dependency dependency : dependencies) {
            // todo: download progress is not visible
            System.out.println("preparing " + dependency.getName() + "...");
            dependency.resolve();
        }
    }

    private YOLONetwork createYOLONetwork(Dependency model, Dependency weights, Dependency names, int size) {
        prepareDependencies(model, weights, names);

        YOLONetwork network = new YOLONetwork(
                model.getPath(),
                weights.getPath(),
                size, size
        );

        network.loadNames(names.getPath());
        return network;
    }

    public YOLONetwork createYOLOv3() {
        return createYOLONetwork(Repository.YOLOv3Model, Repository.YOLOv3Weight, Repository.COCONames, 608);
    }

    public YOLONetwork createYOLOv3Tiny() {
        return createYOLONetwork(Repository.YOLOv3TinyModel, Repository.YOLOv3TinyWeight, Repository.COCONames, 416);
    }

    public YOLONetwork createYOLOv3SPP() {
        return createYOLONetwork(Repository.YOLOv3SPPModel, Repository.YOLOv3SPPWeight, Repository.COCONames, 608);
    }

    public SingleHumanPoseEstimationNetwork createSingleHumanPoseEstimation() {
        prepareDependencies(Repository.SingleHumanPoseEstimationModel);
        return new SingleHumanPoseEstimationNetwork(Repository.SingleHumanPoseEstimationModel.getPath());
    }
}
