package com.topjohnwu.magisk.ui.module

import android.os.Bundle
import android.view.View
import com.topjohnwu.magisk.R
import com.topjohnwu.magisk.arch.BaseFragment
import com.topjohnwu.magisk.core.Config
import com.topjohnwu.magisk.databinding.FragmentSettingsRepoBinding

class RepoSettingsFragment : BaseFragment<FragmentSettingsRepoBinding>() {

    override val layoutRes = R.layout.fragment_settings_repo

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.repoUrlEditText.setText(Config.moduleRepoUrl)
        binding.saveButton.setOnClickListener {
            Config.moduleRepoUrl = binding.repoUrlEditText.text.toString()
            activity?.onBackPressed()
        }
    }
}
