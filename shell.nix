with import <nixpkgs> {};

let
  jdk = adoptopenjdk-bin;
#   jdk = openjdk8;
in
stdenv.mkDerivation {
  name = "jdk";
  buildInputs = [
    jdk
    leiningen
  ];

  shellHook = ''
    export JAVA_HOME=${jdk.home}
  '';
}
