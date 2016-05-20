(function () {

    angular
        .module('MDC')
        .controller('ReportController', [
            'commonService', '$log', '$state', '$scope', '$q', '$http',
            ReportController
        ]);

    /**
     * Manages basic information, e.g. the existing users
     */
    function ReportController(commonService, $log, $state, $scope, $q, $http) {
        var self = this;

        self.statistics = {};
        self.common = commonService;

        self.report = {};

        self.customers = [];

        $http.get("/customers").then(function(resp){
            self.customers = resp.data.content;
        },function(resp){

        });

        self.query = function () {

            self.drawChart();
            $scope.getTransactions();
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

        var transactions_data = {
            "count": 9,
            "data": [{
                "transactionDate": new Date(),
                "iban": "AT123851596451426498",
                "company": "BILLA",
                "description": "Text text",
                "amount": 123.30,
                "currency": "Euro"}]
        };

        $scope.selected = [];

        $scope.query = {
            order: 'name',
            limit: 5,
            page: 1
        };
        $scope.transactions = [];

        function success(transactions) {
            $scope.transactions = transactions.data;
        }

        $scope.getTransactions = function () {
            $scope.promise = $http.get("/transactions/"+ self.report.customerId).then(success);

            //$scope.promise = $q.when(transactions_data,success);
            //$scope.promise = $nutrition.desserts.get($scope.query, success).$promise;
        };
    }

})();
