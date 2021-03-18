package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.database.AsteroidDataBase
import com.udacity.asteroidradar.database.AsteroidDatabaseDao
import com.udacity.asteroidradar.database.toParcelable
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val application = requireNotNull(activity).application

        val dataSource = AsteroidDataBase.getInstance(application).asteroidDatabaseDao

//        populateAsteroids(dataSource)

        val viewModelFactory = MainViewModelFactory(dataSource, application)

        val viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        viewModel.populateAsteroids()

        binding.viewModel = viewModel

        viewModel.asteroidDetail.observe(viewLifecycleOwner, Observer {
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

        var totalAsteroids = viewModel.asteroids.value

        viewModel.asteroids.observe(viewLifecycleOwner, Observer {
            it?.let { adapter.addHeaderAndSubmitList(it.toParcelable()) }
        })

        setHasOptionsMenu(true)

        return binding.root
    }

//    private fun populateAsteroids(dataSource: AsteroidDatabaseDao) {
//        lifecycleScope.launch(Dispatchers.IO){
//            if(dataSource.getAll().value == null){
//                dataSource.insert(Asteroid(
//                    codename = "Just a dummy asteroid"))
//            }
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}