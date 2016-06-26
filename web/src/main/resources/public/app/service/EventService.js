(function(){
    'use strict';

    angular.module('MDC')
        .service('EventService', ['$q','$http', '$timeout', '$mdToast', EventService]);

    function EventService($q, $http, $timeout, $mdToast){
        var self = this;

        var timeoutPromise = null;
        var userId = null;
        var currentToast = null;
        var displayedEvents = [];

        var success = function (resp) {
            if(angular.isArray(resp.data) && resp.data.length > 0) {
                console.log(resp.data);
                console.log(displayedEvents);

                var val = undefined;
                var i = 0;
                //Find first event that wasn't displayed already
                while(val == undefined && i < resp.data.length) {
                    if(!displayedEvents.includes(resp.data[i].threshold.id)) {
                        val = resp.data[i];
                    }
                    i++;
                }

                if(val != undefined) {
                    displayedEvents.push(val.threshold.id);
                    
                    var text = "";
                    var pronome = (val.customer.gender == "Male" ? "his" : "her");
                    if(val.threshold > 0) {
                        text =   val.customer.firstName + " "
                            + val.customer.lastName
                            + " reached "
                            + pronome
                            + " saving goal of € "
                            + val.threshold.threshold
                            ;
                    } else {
                        text =   val.customer.firstName + " "
                            + val.customer.lastName
                            + " spent "
                            + pronome
                            + " spending limit of € "
                            + Math.abs(val.threshold.threshold)
                            ;
                    }

                    if(val.threshold.classification != "") {
                        text += " in category " + val.threshold.classification;
                    }

                    if(val.threshold.windowSize ) {
                        text += " for the last "
                            +  (val.threshold.windowSize / (1000*60*60*24))
                            +  " days"
                            ;
                    }
                    text += "!";
                    console.log(val);
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
