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

        self.download = function() {
            var f = commonService.from.toISOString().slice(0,10);
            var t = commonService.till.toISOString().slice(0,10);
            dashboardService
                .loadStatisticsCsv(f, t)
                .then( function( statistics ) {
                    var data = JSON.stringify(statistics.data);
                    var blob = new Blob([data], {type: "text/plain;charset=utf-8"});
                    saveAs(blob, "data.txt");
                });
        };

        function drawChart() {

            var f = commonService.from.toISOString().slice(0,10);
            var t = commonService.till.toISOString().slice(0,10);
            dashboardService
                .loadStatistics(f, t)
                .then( function( statistics ) {
                    self.statistics    = statistics.data;
                    addColumnChart(statistics.data);
                },function(){
                    

                    /*[{"name":"bauen","percentage":0.32,"total":3200}*/
                    addColumnChart({
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

        function addColumnChart(data){
            var arr = [];
            arr.push(["Classification","TransactionPercentage","CustomerPercentage"]);
            for(var i = 0, length = data.classifications.length; i < length; i++){
                var d = data.classifications[i];
                arr.push([d.name, d.transactionsPercentage, d.customersPercentage]);
            }
            var data = google.visualization.arrayToDataTable(arr);

            var view = new google.visualization.DataView(data);
            /*
            view.setColumns([0, 1,
                { calc: "stringify",
                    sourceColumn: 1,
                    type: "string",
                    role: "annotation" },
                2]);
            */
            var options = {
                title: "Classifications",
                width: 400,
                height: 300,
                bar: {groupWidth: "95%"},
                legend: { position: "none" }
            };
            var chart = new google.visualization.ColumnChart(document.getElementById("columnchart"));
            chart.draw(view, options);
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
