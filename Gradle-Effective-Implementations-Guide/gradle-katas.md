No project:
- Copy files;
- Rename files;
- Zip files;
- Unzip files;
- Tar files;
- Untar files;
- Copy trees;
- Copy with inclusive/exclusive;
- Exec: run scripts;

Single project:
- Package jar;
- Package fat jar;
- Package war;
- Package war and start tomcat manually;
- Package war and start tomcat with that war;
- Add additional configuration;
- Incremental tasks (skip when it was already done);
- Do task only if necessary;

Multi project:
- Package jar;
- Package fat jar;
- Package war;
- Package war and start tomcat;
- Add additional source sets and configurations;
- Manage dependencies between projects;


# No Project

## Copy files

```shell
├── build.gradle
├── file-1
├── file-2
├── folder-1
└── folder-2
```

```groovy
task copyTask(type: Copy) {
	println "Copying files..."
	from("$projectDir/file-1")
	into("$projectDir/folder-1")

	finalizedBy 'printTree', 'deleteFile'
}

task printTree(type: Exec) {
	println 'Printing tree'
	println copyTask.destinationDir
	commandLine 'tree'
}

task deleteFile(type: Delete) {
	println 'Deleting'
	delete fileTree("$copyTask.destinationDir").matching {
		include "**"
	}
}
```

## Rename files

```groovy
task renameFile (type: Copy) {
  println "Renaming..."
	from("$projectDir/file-1")
	into("$projectDir")
	rename {
		"new-name"
	}
}
```

Or:

```groovy
task renameFile {
	doLast {
		def f = new File("$projectDir/file-1")
		f.renameTo("new-name")
	}
}
```

## Zip files

## Unzip files
