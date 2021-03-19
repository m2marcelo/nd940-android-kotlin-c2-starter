package com.udacity.asteroidradar.main

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.dependencyinjection.DependencyInjection
import com.udacity.asteroidradar.database.AsteroidDataBase
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.utils.AsteroidFilter


class MainFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val application = requireNotNull(activity).application

        val dataInjection = DependencyInjection()

        AsteroidDataBase.getInstance(application)

        val viewModelFactory = MainViewModelFactory(dataInjection.repository, application)

        val viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        mainViewModel = viewModel

        binding.viewModel = viewModel

        viewModel.showAsteroidDetail.observe(viewLifecycleOwner, Observer {
                asteroid -> asteroid?.let {
            this.findNavController().navigate(MainFragmentDirections
                .actionShowDetail(asteroid))
            viewModel.onAsteroidNavigated()
        }
        })

        val manager = LinearLayoutManager(context)
        binding.asteroidRecycler.layoutManager = manager

        val adapter = AsteroidAdapter(AsteroidListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid)
        })

        binding.asteroidRecycler.adapter = adapter

        viewModel.asteroids.observe(viewLifecycleOwner, Observer {
            it?.let { adapter.addHeaderAndSubmitList(it) }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.show_all_menu -> mainViewModel.setFilter(AsteroidFilter.WEEKLY)
            R.id.show_rent_menu -> mainViewModel.setFilter(AsteroidFilter.DAILY)
            R.id.show_buy_menu -> mainViewModel.setFilter(AsteroidFilter.NO_FILTER)
            else -> mainViewModel.setFilter(AsteroidFilter.NO_FILTER)
        }
        return true
    }
}