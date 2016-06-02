(function () {

    angular
        .module('MDC')
        .controller('ReportController', [
            'commonService', '$log', '$state', '$scope', '$q', '$http', '$stateParams',
            'CustomerService', 'ReportHistoryService',
            ReportController
        ]);

    /**
     * Manages basic information, e.g. the existing users
     */
    function ReportController(commonService, $log, $state, $scope, $q, $http, $stateParams, CustomerService, ReportHistoryService) {
        var self = this;

        self.statistics = {};
        self.common = commonService;
        self.customer = {};

        self.report = {};
        self.report.customerId = $stateParams.customerId;

        self.customers = [];
        self.categories = [];
        self.locations = [{id:0,name:"Wien"}];

        self.genders = [{
            id: 0,
            name: "undefined"
        },{
            id: 1,
            name: "male"
        },{
            id: 2,
            name: "female"
        }];

        CustomerService.getCustomer($stateParams.customerId)
            .then(function(resp){
                self.customer = resp.data;
            },function(resp){
                alert(resp);
            });


        self.queryLocation   = queryLocation;
        self.selectedLocationChange = selectedLocationChange;
        self.searchLocationChange = searchLocationChange;
        self.drawLineChart = drawLineChart;


        $http.get("/customers").then(function(resp){
            self.customers = resp.data.content;
        },function(resp){

        });

        $http.get("/classifications").then(function(resp){
            console.log("/classifications", resp.data);
            self.categories = resp.data.content;
        },function(resp){

        });

        $http.get("/locations").then(function(resp){
            console.log("/locations", resp.data);
            self.categories = resp.data.content;
        },function(resp){

        });

        self.query = function () {
            self.loadReport();
            $scope.getTransactions();
            self.loadHistoryData();

            ReportHistoryService.addToHistory(self.report);
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

        function buildRequestParams(){
            return angular.extend({}, self.report);
        }

        self.loadReport = function (){
            $http.get("/reports/customers/" + self.report.customerId,{
                params: buildRequestParams()
            }).then(function(resp){
                self.drawChart("piechart", resp.data.customer);
                self.drawChart("groupPieChart", resp.data.group);
            },function(resp){
                self.drawChart("piechart", report_dummy_data);
            });
        };

        self.drawChart = function (elmId, data) {
            var data_arr = [];
            data_arr.push(data.headers);
            data_arr = data_arr.concat(data.data);

            var data = google.visualization.arrayToDataTable(data_arr);

            var options = {
                title: 'Spend money per category'
            };

            var chart = new google.visualization.PieChart(document.getElementById(elmId));

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
            $scope.promise = $http.get("/transactions/"+ self.report.customerId,{
                params: $scope.query
            }).then(success);
        };


        /* Autocomplete */

        /**
         * Search for states... use $timeout to simulate
         * remote dataservice call.
         */
        function queryLocation (query) {
            if(self.locations){
                return query ? self.locations.filter( createFilterFor(query) ) : [];
            }

            return $http.get("/locations").then(function(resp){
                self.locations = resp.data;
                return query ? resp.data.filter( createFilterFor(query) ) : [];
            }, function(resp){
                return [];
            });
        }

        function searchLocationChange(text) {
            $log.info('Text changed to ' + text);
        }
        function selectedLocationChange(item) {
            $log.info('Item changed to ' + JSON.stringify(item));
        }
        /**
         * Create filter function for a query string
         */
        function createFilterFor(query) {
            var lowercaseQuery = angular.lowercase(query);
            return function filterFn(state) {
                return (state.name.toLowerCase().indexOf(lowercaseQuery) === 0);
            };
        }


        /* draw chart*/

        self.loadHistoryData = function () {
            $http.get("/customers/" + self.report.customerId + "/comparison",{
                params: buildRequestParams()
            }).then(function(resp){
                self.drawLineChart(resp.data);
            },function(resp){
                self.drawLineChart();
            });
        };

        function drawLineChart(respData){

            var data = new google.visualization.DataTable();
            data.addColumn('number', 'Day');
            data.addColumn('number', 'Guardians of the Galaxy');
            data.addColumn('number', 'The Avengers');
            data.addColumn('number', 'Transformers: Age of Extinction');

            if(respData){

            }else{
                data.addRows([
                    [1,  37.8, 80.8, 41.8],
                    [2,  -30.9, -69.5, -32.4],
                    [3,  25.4,   57, 25.7],
                    [4,  11.7, 18.8, 10.5],
                    [5,  11.9, 17.6, 10.4],
                    [6,   8.8, 13.6,  7.7],
                    [7,   7.6, 12.3,  9.6],
                    [8,  12.3, 29.2, 10.6],
                    [9,  16.9, 42.9, 14.8],
                    [10, 12.8, 30.9, 11.6],
                    [11,  5.3,  7.9,  4.7],
                    [12,  6.6,  8.4,  5.2],
                    [13,  4.8,  6.3,  3.6],
                    [14,  4.2,  6.2,  3.4]
                ]);
            }

            var options = {
                chart: {
                    title: 'History per Category',
                    subtitle: 'in Euro'
                },
                width: 900,
                height: 500
            };

            var chart = new google.charts.Line(document.getElementById('linechart'));

            chart.draw(data, options);
        }

    }

})();
