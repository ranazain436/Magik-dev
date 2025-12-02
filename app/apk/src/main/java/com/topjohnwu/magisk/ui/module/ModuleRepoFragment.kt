package com.topjohnwu.magisk.ui.module

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.topjohnwu.magisk.R
import com.topjohnwu.magisk.arch.BaseFragment
import com.topjohnwu.magisk.core.model.module.Module
import androidx.lifecycle.lifecycleScope
import com.topjohnwu.magisk.core.di.ServiceLocator
import com.topjohnwu.magisk.databinding.FragmentModuleRepoBinding
import kotlinx.coroutines.launch
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.topjohnwu.magisk.core.model.module.GithubRepo
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collect

class ModuleRepoFragment : BaseFragment<FragmentModuleRepoBinding>() {

    override val layoutRes = R.layout.fragment_module_repo

    private val viewModel: ModuleRepoViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val adapter = ModuleRepoAdapter { module ->
            viewModel.installModule(module)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        viewModel.fetchModules()

        lifecycleScope.launchWhenStarted {
            viewModel.modules.collect { modules ->
                adapter.submitList(modules)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.isLoading.collect { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.hasError.collect { hasError ->
                binding.errorText.visibility = if (hasError) View.VISIBLE else View.GONE
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.installStatus.collect { status ->
                when (status) {
                    is InstallStatus.Error -> {
                        Snackbar.make(binding.root, "Failed to install module", Snackbar.LENGTH_SHORT).show()
                    }
                    else -> {
                        // Do nothing
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_module_repo, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                findNavController().navigate(R.id.action_moduleRepoFragment_to_repoSettingsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
