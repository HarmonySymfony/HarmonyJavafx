package controllers;

import entities.Posts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import services.PostsServices;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class forumStats {

    @FXML
    private BarChart<String, Number> postCommentChart;
    @FXML
    private NumberAxis yAxis; // Ensure this is linked with the FXML if using FXML, or initialized if created in code

    public void initialize() {
        loadChartData();
        configureNumberAxis();
    }

    private void configureNumberAxis() {
        if (yAxis != null) {
            yAxis.setTickLabelFormatter(new StringConverter<>() {
                @Override
                public String toString(Number object) {
                    return String.format("%d", object.intValue());
                }

                @Override
                public Number fromString(String string) {
                    try {
                        return Integer.parseInt(string);
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                }
            });

            yAxis.setAutoRanging(false);
            yAxis.setLowerBound(0);
            yAxis.setUpperBound(findUpperBound());
            yAxis.setTickUnit(1);
        } else {
            System.out.println("NumberAxis not initialized");
        }
    }

    private int findUpperBound() {
        int maxComments = 0;
        if (!postCommentChart.getData().isEmpty() && !postCommentChart.getData().get(0).getData().isEmpty()) {
            for (XYChart.Data<String, Number> data : postCommentChart.getData().get(0).getData()) {
                maxComments = Math.max(maxComments, data.getYValue().intValue());
            }
            return maxComments + 1;
        } else {
            System.out.println("No data available to determine upper bound");
            return 10;
        }
    }

    private void loadChartData() {
        PostsServices postsServices = new PostsServices();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Comments per Post");

        try {
            List<Map<String, Object>> postData = postsServices.getPostsWithCommentCount();
            if (postData.isEmpty()) {
                System.out.println("No data found.");
                return;
            }
            for (Map<String, Object> post : postData) {
                Integer postId = (Integer) post.get("id");
                Integer commentCount = (Integer) post.get("comment_count");

                XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(String.valueOf(postId), commentCount);
                StackPane node = new StackPane();
                node.setOnMouseClicked(event -> openDetailsPost(postId));
                dataPoint.setNode(node);
                series.getData().add(dataPoint);
            }
            postCommentChart.getData().add(series);
            postCommentChart.setLegendVisible(false);
        } catch (SQLException e) {
            System.out.println("Error loading chart data: " + e.getMessage());
        }
    }

    private void openDetailsPost(int postId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/detailsPost.fxml"));
            Parent root = loader.load();

            detailsPost detailsController = loader.getController();
            detailsController.loadPostDetails(postId);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Post Details");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load the details view.");
        }
    }
}
