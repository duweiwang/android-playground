## 第一章 介绍

本章主要介绍JNI技术。JNI是本地编程接口。它允许跑在java虚拟机里的java代码和
使用其他语言例如C，C++等编写的应用程序或库进行交互。

JNI最重要的好处是它不限制底层Java VM的实现，因此，Java VM供应商可以添加对JNI的支持，而不会影响VM的其他部分。
程序员可以编写一个本机应用程序或库的版本，并期望它与支持JNI的所有Java vm一起工作。

本章包含以下主题：
+ java本地编程接口概览
+ 历史背景
    - JDK 1.0本地方法接口
    - Java运行时接口
    - 原生本地接口和Java/COM接口
+ 目标
+ Java本地接口方法
+ Programming to the JNI
+ Changes

Java 本地接口概览
当你完全使用java语言写一款应用的时候，总有Java语言无法满足需求的情况出现，
程序员通过使用JNI编写java本地方法去处理这种情况。

以下阐述了你需要使用JNI的情况：
+ The standard Java class library does not support the platform-dependent features needed by the application.
+ You already have a library written in another language, and wish to make it accessible to Java code through the JNI.
+ You want to implement a small portion of time-critical code in a lower-level language such as assembly.

通过使用JNI，你可以使用本地方法完成：
+ Create, inspect, and update Java objects (including arrays and strings).
+ Call Java methods.
+ Catch and throw exceptions.
+ Load classes and obtain class information.
+ Perform runtime type checking.

您还可以使用JNI的Invocation API来支持任意的本地应用程序来嵌入Java VM。 This allows programmers
to easily make their existing applications Java-enabled without having to link with the VM source code.
