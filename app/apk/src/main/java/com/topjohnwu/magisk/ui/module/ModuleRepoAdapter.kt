package com.topjohnwu.magisk.ui.module

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.topjohnwu.magisk.databinding.ItemModuleRepoBinding
import com.topjohnwu.magisk.core.model.module.GithubRepo

class ModuleRepoAdapter(private val installCallback: (GithubRepo) -> Unit) :
    ListAdapter<GithubRepo, ModuleRepoAdapter.ViewHolder>(ModuleDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemModuleRepoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, installCallback)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemModuleRepoBinding,
        private val installCallback: (GithubRepo) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(module: GithubRepo) {
            binding.moduleName.text = module.name
            binding.moduleAuthor.text = module.author
            binding.moduleDescription.text = module.description

            binding.installButton.setOnClickListener {
                installCallback(module)
            }
        }
    }
}

class ModuleDiffCallback : DiffUtil.ItemCallback<GithubRepo>() {
    override fun areItemsTheSame(oldItem: GithubRepo, newItem: GithubRepo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GithubRepo, newItem: GithubRepo): Boolean {
        return oldItem == newItem
    }
}
