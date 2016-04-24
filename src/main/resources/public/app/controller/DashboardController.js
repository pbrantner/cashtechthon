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
/*
 [
 ["Element", "Density", { role: "style" } ],
 ["Copper", 8.94, "#b87333"],
 ["Silver", 10.49, "silver"],
 ["Gold", 19.30, "gold"],
 ["Platinum", 21.45, "color: #e5e4e2"]
 ]
 */
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
                width: 600,
                height: 400,
                bar: {groupWidth: "95%"},
                legend: { position: "none" }
            };
            var chart = new google.visualization.ColumnChart(document.getElementById("columnchart"));
            chart.draw(view, options);
        }

        function addHistogram() {
            var div = document.getElementById("histogram");
            if(!google.visualization){
                console.error("google.visualization undefined");
                return;
            }
            var data = google.visualization.arrayToDataTable([
                ['Dinosaur', 'Length'],
                ['Acrocanthosaurus (top-spined lizard)', 12.2],
                ['Albertosaurus (Alberta lizard)', 9.1],
                ['Allosaurus (other lizard)', 12.2],
                ['Apatosaurus (deceptive lizard)', 22.9],
                ['Archaeopteryx (ancient wing)', 0.9],
                ['Argentinosaurus (Argentina lizard)', 36.6],
                ['Baryonyx (heavy claws)', 9.1],
                ['Brachiosaurus (arm lizard)', 30.5],
                ['Ceratosaurus (horned lizard)', 6.1],
                ['Coelophysis (hollow form)', 2.7],
                ['Compsognathus (elegant jaw)', 0.9],
                ['Deinonychus (terrible claw)', 2.7],
                ['Diplodocus (double beam)', 27.1],
                ['Dromicelomimus (emu mimic)', 3.4],
                ['Gallimimus (fowl mimic)', 5.5],
                ['Mamenchisaurus (Mamenchi lizard)', 21.0],
                ['Megalosaurus (big lizard)', 7.9],
                ['Microvenator (small hunter)', 1.2],
                ['Ornithomimus (bird mimic)', 4.6],
                ['Oviraptor (egg robber)', 1.5],
                ['Plateosaurus (flat lizard)', 7.9],
                ['Sauronithoides (narrow-clawed lizard)', 2.0],
                ['Seismosaurus (tremor lizard)', 45.7],
                ['Spinosaurus (spiny lizard)', 12.2],
                ['Supersaurus (super lizard)', 30.5],
                ['Tyrannosaurus (tyrant lizard)', 15.2],
                ['Ultrasaurus (ultra lizard)', 30.5],
                ['Velociraptor (swift robber)', 1.8]]);

            var options = {
                title: 'Lengths of dinosaurs, in meters',
                legend: { position: 'none' },
            };

            var chart = new google.visualization.ColumnChart(div);
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
