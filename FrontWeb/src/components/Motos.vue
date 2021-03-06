<template>
  <div id="app">
    <h1 data-test="motos-title">Motos</h1>
    <div class="container" v-if="$store.getters.isLoggedIn">
      <b-table data-test="motos-b-table" responsive striped hover :items="motos" :fields="fields" :filter-function="filterTable" sort-by="id">
        <template #cell(available)="row">
          <b-button :variant="availableButton(row.value)" size="sm" @click="putMotoAvailable(row.item.id, row.value)"> {{row.value}} </b-button>
        </template>
        <template #cell(actions)="row">
          <button class="btn btn-info btn-sm" @click="info(row.item, row.item.id, $event.target)"> json </button>
          <button class="btn btn-warning btn-sm" @click="goToMoto(row.item.id, $event.target)"> moto </button>
          <button class="btn btn-secondary btn-sm" disabled @click="modifyMoto(row.item, $event.target)"> modify </button>
          <button class="btn btn-danger btn-sm" @click="deleteMoto(row.item.id)"> X </button>
        </template>
      </b-table>
      <button class="btn btn-success btn-md" @click="addMoto($event.target)"> Add Moto </button>
      <!-- Modal Json -->
      <b-modal :id="infoModal.id" :title="infoModal.title" ok-only centered @hide="resetInfoModal">
        <pre>{{ infoModal.content }}</pre>
      </b-modal>
      <!-- Modal Add Moto -->
      <b-modal :id="addMotoModal.id" :title="addMotoModal.title" centered  @ok="handleOk" @hidden="resetAddMotoModal">
        <form ref="form" @submit.stop.prevent="handleSubmit">
          <b-form-group
            :state="licenseNumberValidity"
            label="License Number"
            label-for="licence-number-input"
            invalid-feedback="License Number is required"
          >
            <b-form-input
              id="licence-number-input"
              v-model="addMotoModal.moto.license_number"
              :state="licenseNumberValidity"
              required
            ></b-form-input>
          </b-form-group>
          <b-form-group
            :state="batteryValidity"
            label="Battery"
            label-for="battery-input"
            invalid-feedback="Battery is required"
          >
            <b-form-input
              id="battery-input"
              v-model="addMotoModal.moto.battery"
              :state="batteryValidity"
              required
            ></b-form-input>
          </b-form-group>
          <b-form-group
            :state="latitudeValidity"
            label="Latitude"
            label-for="latitude-input"
            invalid-feedback="Latitude is required"
          >
            <b-form-input
              id="latitude-input"
              v-model="addMotoModal.moto.latitude"
              :state="latitudeValidity"
              required
            ></b-form-input>
          </b-form-group>
          <b-form-group
            :state="longitudeValidity"
            label="Longitude"
            label-for="longitude-input"
            invalid-feedback="Longitude is required"
          >
            <b-form-input
              id="longitude-input"
              v-model="addMotoModal.moto.longitude"
              :state="longitudeValidity"
              required
            ></b-form-input>
          </b-form-group>
        </form>
      </b-modal>
    </div>
    <div class="container" v-if="!$store.getters.isLoggedIn">
      <img src="@/assets/stop.jpg" height="200" margin>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'Motos',
  computed: {
    licenseNumberValidity () {
      if (this.addMotoModal.license_number_state === false) { return null } else {
        if (this.addMotoModal.moto.license_number === '') { return false } else { return null }
      }
    },
    batteryValidity () {
      if (this.addMotoModal.battery_state === false) { return null } else {
        if (this.addMotoModal.moto.battery === '') { return false } else { return null }
      }
    },
    latitudeValidity () {
      if (this.addMotoModal.latitude_state === false) { return null } else {
        if (this.addMotoModal.moto.latitude === '') { return false } else { return null }
      }
    },
    longitudeValidity () {
      if (this.addMotoModal.longitude_state === false) { return null } else {
        if (this.addMotoModal.moto.longitude === '') { return false } else { return null }
      }
    }
  },
  data () {
    return {
      motos: [],
      fields: [
        {key: 'id', sortable: true},
        {key: 'license_number', sortable: false},
        {key: 'battery', sortable: true},
        {key: 'available', sortable: true},
        {key: 'latitude', sortable: false},
        {key: 'longitude', sortable: false},
        {key: 'actions', sortable: false}
      ],
      infoModal: {
        id: 'info-modal',
        title: '',
        content: ''
      },
      addMotoModal: {
        id: 'add-moto-modal',
        title: '',
        moto: {
          id: '',
          license_number: '',
          battery: '',
          available: '',
          latitude: '',
          longitude: ''
        },
        license_number_state: false,
        battery_state: false,
        latitude_state: false,
        longitude_state: false
      },
      isPostNotPut: true
    }
  },
  methods: {
    getMotos () {
      const path = this.$heroku + '/motos'
      axios.get(path, {
        auth: { username: this.token }
      })
        .then((res) => {
          this.motos = res.data.motos
        })
        .catch((error) => {
          console.error(error)
        })
    },
    deleteMoto (motoId) {
      const path = this.$heroku + `/moto/${motoId}`
      axios.delete(path, {
        auth: { username: this.token }
      })
        .then((res) => {
          console.log(res)
          this.getMotos()
        })
        .catch((error) => {
          console.error(error)
        })
    },
    addMoto (button) {
      this.isPostNotPut = true
      this.openMotoModal('Add a new Moto', button)
    },
    modifyMoto (moto, button) {
      this.isPostNotPut = false
      this.addMotoModal.moto = moto
      this.openMotoModal(`Modify Moto with id: ${moto.id}`, button)
    },
    openMotoModal (title, button) {
      this.addMotoModal.title = title
      this.$root.$emit('bv::show::modal', this.addMotoModal.id, button)
    },
    checkFormValidity () {
      const valid = this.$refs.form.checkValidity()
      this.addMotoModal.license_number_state = true
      this.addMotoModal.battery_state = true
      this.addMotoModal.latitude_state = true
      this.addMotoModal.longitude_state = true
      return valid
    },
    handleOk (bvModalEvt) {
      bvModalEvt.preventDefault()
      this.handleSubmit()
    },
    handleSubmit () {
      if (!this.checkFormValidity()) {
        return
      }
      if (this.isPostNotPut) {
        this.postMoto()
      } else {
        this.putMoto()
      }
      this.$nextTick(() => {
        this.resetAddMotoModal()
        this.$bvModal.hide(this.addMotoModal.id)
      })
    },
    postMoto () {
      const path = this.$heroku + `/moto`
      const moto = {
        'license_number': this.addMotoModal.moto.license_number,
        'battery': this.addMotoModal.moto.battery,
        'latitude': this.addMotoModal.moto.latitude,
        'longitude': this.addMotoModal.moto.longitude
      }
      axios.post(path, moto, {
        auth: { username: this.token }
      })
        .then((res) => {
          console.log(res)
          this.getMotos()
        })
        .catch((error) => {
          console.error(error)
          this.getMotos()
        })
    },
    putMoto () {
      /* const moto = {
        'license_number': this.addMotoModal.moto.license_number,
        'battery': this.addMotoModal.moto.battery,
        'latitude': this.addMotoModal.moto.latitude,
        'longitude': this.addMotoModal.moto.longitude
      } */
      /* const path = this.$heroku + `/moto/${this.addMotoModal.moto.id}`
      const moto = { available: this.addMotoModal.moto.available }
      axios.put(path, moto)
        .then((res) => {
          console.log(res)
          this.getMotos()
        })
        .catch((error) => {
          console.error(error)
          this.getMotos()
        }) */
    },
    resetAddMotoModal () {
      this.addMotoModal = {
        id: 'add-moto-modal',
        title: '',
        moto: {
          id: '',
          license_number: '',
          battery: '',
          available: '',
          latitude: '',
          longitude: ''
        },
        license_number_state: false,
        battery_state: false,
        latitude_state: false,
        longitude_state: false
      }
    },
    putMotoAvailable (id, available) {
      const path = this.$heroku + `/moto/${id}`
      const param = {'available': !available}
      axios.put(path, param, {
        auth: { username: this.token }
      })
        .then((res) => {
          console.log(res)
          this.getMotos()
        })
        .catch((error) => {
          console.error(error)
          this.getMotos()
        })
    },
    availableButton (available) {
      if (available) { return 'outline-success' } else { return 'outline-danger' }
    },
    goToMoto (id, button) {
      this.$router.replace({path: `/moto/${id}`})
    },
    info (item, id, button) {
      this.infoModal.title = `Moto Detail of moto ${id}`
      this.infoModal.content = JSON.stringify(item, null, 2)
      this.$root.$emit('bv::show::modal', this.infoModal.id, button)
    },
    resetInfoModal () {
      this.infoModal.title = ''
      this.infoModal.content = ''
    },
    filterTable (row, filter) {
      if (this.filterOn.includes('moto_id')) {
        if (row.moto_id.toString() === filter) {
          return true
        } else {
          return false
        }
      } else if (this.filterOn.includes('user_id')) {
        if (row.user_id.toString() === filter) {
          return true
        } else {
          return false
        }
      }
    }
  },
  created () {
    this.getMotos()
    this.token = this.$store.state.token
  }
}
</script>
