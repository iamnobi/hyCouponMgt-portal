#/bin/sh

jar_name=ocean-ds-portal.jar
./gradlew clean
./gradlew bootJar
cd ./build/libs

# 因為弱點掃描會掃出弱點,因此將以下不必要的檔案移除
echo '\nDelete file in zip'
echo '---------------------------------------------------------------------'
zip -d $jar_name 'BOOT-INF/classes/application-*.properties'
zip -d $jar_name 'BOOT-INF/classes/com/cherri/acs_kernel/plugin/hsm/impl/MockHsmImpl.class'
zip -d $jar_name 'BOOT-INF/classes/com/cherri/acs_kernel/plugin/hsm/impl/MockHsmImpl$1.class'
zip -d $jar_name 'BOOT-INF/classes/com/cherri/acs_kernel/plugin/hsm/impl/model/domain/MockHsmKey.class'
zip -d $jar_name 'BOOT-INF/classes/com/cherri/acs_kernel/plugin/hsm/impl/model/dao/MockHsmKeyDAO.class'
echo '---------------------------------------------------------------------\n'

openssl dgst -md5 "./$jar_name"
openssl dgst -sha1 "./$jar_name"
