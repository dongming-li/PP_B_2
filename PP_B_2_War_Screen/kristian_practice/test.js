var mysql = require('mysql');

var con = mysql.createConnection({
  host: " mysql.cs.iastate.edu",
  user: "dbu309amb4",
  password: "This-is-not-a-correct-password",
  database: "db309amb4"
});
con.connect(function(err) {
  if (err) throw err;
  console.log("Connected!");
  var sql = "CREATE TABLE players (id INT AUTO_INCREMENT PRIMARY KEY, user VARCHAR(25))";
  con.query(sql, function (err, result) {
    if (err) throw err;
    console.log("Table created");
  });
});