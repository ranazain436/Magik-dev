package com.topjohnwu.magisk.core.model.module

abstract class Module : Comparable<Module> {
    abstract var id: String
        protected set
    abstract var name: String
        protected set
    abstract var version: String
        protected set
    abstract var versionCode: Int
        protected set
    open var author: String = ""
        protected set
    open var description: String = ""
        protected set

    override operator fun compareTo(other: Module) = id.compareTo(other.id)
}
