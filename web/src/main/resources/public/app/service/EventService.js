(function(){
    'use strict';

    angular.module('MDC')
        .service('EventService', ['$q','$http', '$timeout', '$mdToast', EventService]);

    function EventService($q, $http, $timeout, $mdToast){
        var self = this;

        var timeoutPromise = null;
        var userId = null;
        var currentToast = null;

        var success = function (resp) {
            if(angular.isArray(resp.data) && resp.data.length > 0) {
                var val = resp.data[0];
                var text = val.id + ": " + val.threshold.threshold + " for " + val.threshold.classification + " reached!";
                var simple = $mdToast.simple().textContent(text).action("OK").highlightAction(true).position("top right").hideDelay(false);
                if(currentToast == null){
                    currentToast = $mdToast.show(simple).then(function (answer) {
                        if (answer === "ok") {
                            delNotification(val.id);
                            currentToast = null;
                        }
                    });
                }
            }
        };

        var delNotification = function (id) {
            return $http.delete("/customers/events/" + id ).then(getNotifications);
        };

        var getNotifications = function () {
            var query = {userId:userId};
            return $http.get("/customers/" + userId + "/events/",{
                params : query
            }).then(success);
        };

        var clearNotifications = function () {
            $mdToast.hide();
        };

        return {
            clearNotifications: clearNotifications,
            getNotifications : getNotifications,
            connectUserId : function (id) {
                // TODO start poll cycle
                clearNotifications();
                currentToast = null;

                userId = id;
                console.log("connect notification for user:", userId);
                $timeout.cancel(timeoutPromise);

                var poll = function () {
                    timeoutPromise = $timeout(function () {
                        getNotifications().then(function () {
                            poll();
                        });
                    },5000);
                };
                poll();

                getNotifications();
            }
        };
    }

})();