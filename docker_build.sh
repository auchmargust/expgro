rootPath=`pwd`
echo $rootPath

echo '================================= Build project ==============================>>>'
mvn clean package

echo '================================= Build docker images ========================>>>'

cd grocery-express-customer
pwd
docker build -t grocery-express/customer .

cd $rootPath
cd grocery-express-store
docker build -t grocery-express/store .

echo '================================= Build docker images success 👌🏻👌🏻👌🏻===========>>>'

docker images | grep grocery-express
