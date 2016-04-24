(function(){

    angular
        .module('MDC')
        .controller('DashboardController', [
            'dashboardService', 'commonService', '$log', '$state',
            DashboardController
        ]);

    /**
     * Manages basic information, e.g. the existing users
     */
    function DashboardController(dashboardService, commonService, $log, $state ) {
        var self = this;

        self.statistics        = { };
        self.common = commonService;

        function drawChart(){

            var f = commonService.from.toISOString().slice(0,10);
            var t = commonService.till.toISOString().slice(0,10);
            dashboardService
                .loadStatistics(f, t)
                .then( function( statistics ) {
                    self.statistics    = statistics.data;
                    addBarChart(statistics.data);
                },function(){
                    /*[{"name":"bauen","percentage":0.32,"total":3200}*/
                    addBarChart({
                        classifications : [{"name":"bauen","transactions":3200,"transactionsPercentage":0.32,"customers":54,"customersPercentage":0.36}
                            ,{"name":"mode","transactions":5000,"transactionsPercentage":0.5,"customers":109,"customersPercentage":0.73}
                            ,{"name":"sparen","transactions":10000,"transactionsPercentage":1,"customers":67,"customersPercentage":0.45}
                        ]
                    });
                });

        }
        drawChart();

        self.changeDate = function(){
            drawChart();
        };

        function addBarChart(data){
            var arr = [];
            arr.push(['Classification', 'Transactions']);
            for(var idx=0; idx < data.classifications.length; idx++){
                var cl = data.classifications[idx];
                arr.push([cl.name, cl.transactions]);
            }

            var data = google.visualization.arrayToDataTable(arr);

            var options = {
                chart: {
                    title: 'Transactions'
                },
                legend: { position: 'none' },
                axes: {
                    x: {
                        0: { side: 'top', label: 'Transactions'} // Top x-axis.
                    }
                },
                bars: 'horizontal' // Required for Material Bar Charts.
            };

            var chart = new google.charts.Bar(document.getElementById('barchart'));

            chart.draw(data, options);
        }

        self.showDashboard = function(){
            $state.go('dashboard');
        };

        self.selectUser = function(){
            $state.go('user');
        };

        self.showUpload = function(){
            console.log("test");

            var form = document.getElementById('file-form');
            var fileSelect = document.getElementById('file-select');
            var uploadButton = document.getElementById('upload-button');

            document.querySelector('#file-select').click();
            document.querySelector('#file-select').onchange = function(evt){

                var input = fileSelect;

                var data = new FormData();
                data.append('file', input.files[0]);
                data.append('user', 'hubot');

                fetch('/files', {
                    method: 'POST',
                    credentials: 'include',
                    body: data
                }).then(function(data){
                    console.log(data);
                });
            };
        };
    }



})();
