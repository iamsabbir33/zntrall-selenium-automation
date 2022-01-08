
# Zntrall

Instructions for how to run the scripts


## Documentation

[Documentation](https://www.selenium.dev/documentation/grid/)


## Prerequisite to run the grid 

- Google chrome (If you want to run it on chrome)-- install it
    - For chrome, you need chrome web driver – download it (https://chromedriver.storage.googleapis.com/index.html?path=96.0.4664.45/ )
- Firefox (If you want to run it on firefox) – install it
    - For firefox, you need gecko driver – download it (https://github.com/mozilla/geckodriver/releases )
- Selenium standalone server – download it (https://selenium-release.storage.googleapis.com/3.5/selenium-server-standalone-3.5.3.jar )
- Put all the downloaded files in a folder so that it can be easily accessed.

## How to run the scripts

1. Start selenium standalone server first 
    a. Go to the directory where the standalone jar file is located. Open command line prompt on the folder and run the following command and minimize it: “java -jar selenium-server-standalone-3.5.3.jar -role hub”

    b. Open another command line prompt on the same directory and run the command and minimize it (for chrome): “java -jar selenium-server-standalone-3.5.3.jar -role node -hub http://192.168.31.17:4444/grid/register” 

    c. Open another command line prompt on the same directory and run the command and minimize it (for firefox): “Java -Dwebdriver.gecko.driver=”put the firefox gecko driver directory link within this quote” -jar selenium-server-standalone-3.5.3.jar -role node -hub http://192.168.31.17:4444/grid/register” 
2. Change the http hub link from (b) or c) after running the command from (a) and copy the hub link from that and use the hub link to (b) or c). 
3. Open the project using selenium
4. Open test.xml file
5. Change the Browser name, OS name, Hub link and add/ remove class to run the scripts
6. No need to change any of the scripts individually for now.
7. To change the input data, Go to the "normalUserInputData" package for inserting input for normal users and "superAdminInputData" for Super admin 


