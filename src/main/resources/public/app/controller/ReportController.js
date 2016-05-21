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
            self.loadReport();
            $scope.getTransactions();
        };

        var report_dummy_data = {
            headers : ['Class', 'Spend'],
            data : [
                ['Work', 11],
                ['Eat', 2],
                ['Commute', 2],
                ['Watch TV', 2],
                ['Sleep', 7]
            ]
        };

        self.loadReport = function (){
            $http.get("/reports/customers/" + self.report.customerId,{
                params: {
                    age : self.report.age,
                    income: self.report.income
                }
            }).then(function(resp){
                self.drawChart(resp.data);
            },function(resp){
                self.drawChart(report_dummy_data);
            });
        };

        self.drawChart = function (data) {
            var data_arr = [];
            data_arr.push(data.headers);
            data_arr = data_arr.concat(data.data);

            var data = google.visualization.arrayToDataTable(data_arr);

            var options = {
                title: 'Spend money per category'
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
