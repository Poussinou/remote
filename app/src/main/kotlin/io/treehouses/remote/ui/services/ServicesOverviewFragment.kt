package io.treehouses.remote.ui.services

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import io.treehouses.remote.Constants
import io.treehouses.remote.R
import io.treehouses.remote.Tutorials
import io.treehouses.remote.adapter.ServicesListAdapter
import io.treehouses.remote.callback.ServicesListener
import io.treehouses.remote.databinding.ActivityServicesTabFragmentBinding

class ServicesOverviewFragment() : BaseServicesFragment(), OnItemClickListener {
    private var adapter: ServicesListAdapter? = null
    private var servicesListener: ServicesListener? = null
    private lateinit var bind: ActivityServicesTabFragmentBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getViewModel()
        mChatService = listener.getChatService()
        bind = ActivityServicesTabFragmentBinding.inflate(inflater, container, false)
        viewModel.servicesData.observe(viewLifecycleOwner, Observer {
            adapter = ServicesListAdapter(requireContext(), it, ContextCompat.getColor(requireContext(), R.color.bg_white))
            bind.listView.adapter = adapter
            bind.listView.onItemClickListener = this
        })
        bind.searchBar.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                adapter!!.filter.filter(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {}
        })
        return bind.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Tutorials.servicesOverviewTutorials(bind!!, requireActivity())
    }

    @JvmField
    val handlerOverview: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                Constants.MESSAGE_STATE_CHANGE -> {
                    listener.redirectHome()
                }
            }
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            servicesListener = parentFragment as ServicesListener?
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        val selected = viewModel.servicesData.value!![position]
        if (servicesListener != null) servicesListener!!.onClick(selected)
    }

    companion object {
        private const val TAG = "ServicesTabFragment"
    }

}