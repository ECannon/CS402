const express = require('express');
const request = require('request');
const app = express();
const path = require('path');
const xml2js = require('xml2js');
const parser = new xml2js.Parser();

app.use(express.static(path.join(__dirname, 'public')))
app.use(express.urlencoded({
    extended: true
}))
app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, '/views'))

app.get('/', (req, res) => {
    let URL = "http://api.irishrail.ie/realtime/realtime.asmx/getAllStationsXML";
    request(URL, function (error, response, body) {
        if (!error && response.statusCode == 200) {
            parser.parseString(body, function(error, result){
                if(error === null) {
                    var stationNames = result.ArrayOfObjStation.objStation;
                    console.log("here " + stationNames[0].StationDesc)
                    res.render("index", {stationNames})
                }
                else {
                    console.log(error);
                }
            })
        }

    }); 
})

app.post('/trains', (req, res) => {
    let params = req.body;
    console.log(params);
    let URL = "http://api.irishrail.ie/realtime/realtime.asmx/getStationDataByNameXML?StationDesc="+params.station+"&NumMins="+params.minutes;
    console.log(URL)
    request(URL, function (error, response, body) {
        if (!error && response.statusCode == 200) {
            parser.parseString(body, function(error, result){
                if(error === null) {
                    var trainData = result.ArrayOfObjStationData.objStationData;
                    res.render("table", {trainData});
                }
                else {
                    console.log(error);
                }
            })
        }

    });    
})

app.listen(3000, () => {
  console.log(`Listening at http://localhost:3000`)
})