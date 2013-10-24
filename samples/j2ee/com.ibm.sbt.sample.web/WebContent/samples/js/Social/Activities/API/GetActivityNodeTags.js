require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityService" ], function(dom,json,ActivityService) {
        var activitiesService = new ActivityService();
        var activityNodeId = "%{name=sample.activityNodeId|helpSnippetId=Social_Activities_API_GetActivityNodes}";
        var promise = activitiesService.getActivityNodeTags(activityNodeId, {
                asc : true,
                page : 1,
                ps : 2
            });
        promise.then(
            function(tags) {
                dom.setText("json", json.jsonBeanStringify(tags));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);
