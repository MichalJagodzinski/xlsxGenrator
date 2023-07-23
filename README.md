# xlsxGenerator
This is application to generate data in .xlsx file.

To run application go to application-ci/dev/docker properties, there is a line app.generation.file-location and there you have to type location
where you want save your generated file. Then chose a profile ci/dev/docker and run application,
go to swagger/postman use localhost:8080. Use endpoint /v1/api/generate-mock with json from file exampleJson.txt.

This is all, enjoy!