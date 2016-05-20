(function () {

    angular
        .module('MDC')
        .controller('ReportController', [
            'commonService', '$log', '$state', '$scope', '$q',
            ReportController
        ]);

    /**
     * Manages basic information, e.g. the existing users
     */
    function ReportController(commonService, $log, $state, $scope, $q) {
        var self = this;

        self.statistics = {};
        self.common = commonService;

        self.customers = self.customers || [{
                name: "Test McTestington",
                customerId: 123
            }, {
                name: "Gabriel McKormick",
                customerId: 222
            }];

        self.query = function () {

            self.drawChart();
            $scope.getDesserts();
        };

        self.drawChart = function () {
            var data = google.visualization.arrayToDataTable([
                ['Task', 'Hours per Day'],
                ['Work', 11],
                ['Eat', 2],
                ['Commute', 2],
                ['Watch TV', 2],
                ['Sleep', 7]
            ]);

            var options = {
                title: 'My Daily Activities'
            };

            var chart = new google.visualization.PieChart(document.getElementById('piechart'));

            chart.draw(data, options);
        };


        /* data-table stuff*/


        var desserts_data = {
            "count": 9,
            "data": [
                {
                    "name": "Frozen yogurt",
                    "type": "Ice cream",
                    "calories": {"value": 159.0},
                    "fat": {"value": 6.0},
                    "carbs": {"value": 24.0},
                    "protein": {"value": 4.0},
                    "sodium": {"value": 87.0},
                    "calcium": {"value": 14.0},
                    "iron": {"value": 1.0}
                }, {
                    "name": "Ice cream sandwich",
                    "type": "Ice cream",
                    "calories": {"value": 237.0},
                    "fat": {"value": 9.0},
                    "carbs": {"value": 37.0},
                    "protein": {"value": 4.3},
                    "sodium": {"value": 129.0},
                    "calcium": {"value": 8.0},
                    "iron": {"value": 1.0}
                }, {
                    "name": "Eclair",
                    "type": "Pastry",
                    "calories": {"value": 262.0},
                    "fat": {"value": 16.0},
                    "carbs": {"value": 24.0},
                    "protein": {"value": 6.0},
                    "sodium": {"value": 337.0},
                    "calcium": {"value": 6.0},
                    "iron": {"value": 7.0}
                }, {
                    "name": "Cupcake",
                    "type": "Pastry",
                    "calories": {"value": 305.0},
                    "fat": {"value": 3.7},
                    "carbs": {"value": 67.0},
                    "protein": {"value": 4.3},
                    "sodium": {"value": 413.0},
                    "calcium": {"value": 3.0},
                    "iron": {"value": 8.0}
                }, {
                    "name": "Jelly bean",
                    "type": "Candy",
                    "calories": {"value": 375.0},
                    "fat": {"value": 0.0},
                    "carbs": {"value": 94.0},
                    "protein": {"value": 0.0},
                    "sodium": {"value": 50.0},
                    "calcium": {"value": 0.0},
                    "iron": {"value": 0.0}
                }, {
                    "name": "Lollipop",
                    "type": "Candy",
                    "calories": {"value": 392.0},
                    "fat": {"value": 0.2},
                    "carbs": {"value": 98.0},
                    "protein": {"value": 0.0},
                    "sodium": {"value": 38.0},
                    "calcium": {"value": 0.0},
                    "iron": {"value": 2.0}
                }, {
                    "name": "Honeycomb",
                    "type": "Other",
                    "calories": {"value": 408.0},
                    "fat": {"value": 3.2},
                    "carbs": {"value": 87.0},
                    "protein": {"value": 6.5},
                    "sodium": {"value": 562.0},
                    "calcium": {"value": 0.0},
                    "iron": {"value": 45.0}
                }, {
                    "name": "Donut",
                    "type": "Pastry",
                    "calories": {"value": 452.0},
                    "fat": {"value": 25.0},
                    "carbs": {"value": 51.0},
                    "protein": {"value": 4.9},
                    "sodium": {"value": 326.0},
                    "calcium": {"value": 2.0},
                    "iron": {"value": 22.0}
                }, {
                    "name": "KitKat",
                    "type": "Candy",
                    "calories": {"value": 518.0},
                    "fat": {"value": 26.0},
                    "carbs": {"value": 65.0},
                    "protein": {"value": 7.0},
                    "sodium": {"value": 54.0},
                    "calcium": {"value": 12.0},
                    "iron": {"value": 6.0}
                }
            ]
        };

        $scope.selected = [];

        $scope.query = {
            order: 'name',
            limit: 5,
            page: 1
        };
        $scope.desserts = [];

        function success(desserts) {
            $scope.desserts = desserts;
        }

        $scope.getDesserts = function () {
            $scope.promise = $q.when(desserts_data,success);
            //$scope.promise = $nutrition.desserts.get($scope.query, success).$promise;
        };
    }

})();
